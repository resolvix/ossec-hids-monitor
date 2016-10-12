package com.resolvix.ohm.module.jira

import java.io.File
import java.net.URI
import java.util.Date
import java.util.concurrent.TimeUnit

import com.atlassian.event.api.EventPublisher
import com.atlassian.httpclient.apache.httpcomponents.DefaultHttpClient
import com.atlassian.httpclient.api.{HttpClient, Request}
import com.atlassian.httpclient.api.factory.HttpClientOptions
import com.atlassian.httpclient.spi.ThreadLocalContextManager
import com.atlassian.jira.rest.client.api._
import com.atlassian.jira.rest.client.api.domain.{BasicIssue, BasicProject, Issue, IssueType}
import com.atlassian.jira.rest.client.api.domain.input.{IssueInput, IssueInputBuilder}
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler
import com.atlassian.jira.rest.client.internal.async.{AsynchronousHttpClientFactory, AsynchronousJiraRestClient, AtlassianHttpClientDecorator, DisposableHttpClient}
import com.atlassian.sal.api.ApplicationProperties
import com.atlassian.util.concurrent.{Effect, Promise}

import scala.util.{Failure, Success, Try}

object JiraClient {
  /**
    *
    */
  class JiraRestClientFactory
    extends com.atlassian.jira.rest.client.api.JiraRestClientFactory
  {
    override def createWithBasicHttpAuthentication(
      serverUri: URI,
      userName: String,
      password: String
    ): JiraRestClient = {
      create(
        serverUri,
        new BasicHttpAuthenticationHandler(
          userName,
          password
        )
      )
    }

    private def createDisposableHttpClient(
      serverUri: URI,
      authenticationHandler: AuthenticationHandler
    ): DisposableHttpClient = {

      def applicationProperties(
        serverUri: URI
      ): ApplicationProperties = new ApplicationProperties {
        override def getDisplayName: String = ???
        override def getHomeDirectory: File = new File(".")
        override def getPropertyValue(s: String): String = ???
        override def getBaseUrl: String = serverUri.getPath
        override def getVersion: String = "0.0.1"
        override def getBuildDate: Date = ???
        override def getBuildNumber: String = "0"
      }

      def threadLocalContextManager: ThreadLocalContextManager[Unit]
      = new ThreadLocalContextManager[Unit] {
        override def setThreadLocalContext(c: Unit): Unit = {}
        override def resetThreadLocalContext(): Unit = {}
        override def getThreadLocalContext: Unit = {}
      }

      val noOpEventPub: EventPublisher = new EventPublisher {
        override def unregisterAll(): Unit = {}
        override def register(o: scala.Any): Unit = {}
        override def publish(o: scala.Any): Unit = {}
        override def unregister(o: scala.Any): Unit = {}
      }

      val options: HttpClientOptions = new HttpClientOptions()
      options.setRequestPreparer(
        new Effect[Request] {
          override def apply(
            a: Request
          ): Unit = {
            authenticationHandler.configure(a)
          }
        }
      )

      options.setRequestTimeout(30, TimeUnit.SECONDS)
      options.setConnectionTimeout(30, TimeUnit.SECONDS)

      val defaultHttpClient: DefaultHttpClient[Unit]
      = new DefaultHttpClient[Unit](
        noOpEventPub,
        applicationProperties(serverUri),
        threadLocalContextManager,
        options
      )

      new AtlassianHttpClientDecorator(defaultHttpClient) {
        override def destroy(): Unit = {
          defaultHttpClient.destroy()
        }
      }.asInstanceOf[DisposableHttpClient]
    }

    override def create(
      serverUri: URI,
      authenticationHandler: AuthenticationHandler
    ): JiraRestClient = {
      new AsynchronousJiraRestClient(
        serverUri,
        createDisposableHttpClient(
          serverUri,
          authenticationHandler
        )
      )
    }

    override def create(
      serverUri: URI,
      httpClient: HttpClient
    ): JiraRestClient = {
      new AsynchronousJiraRestClient(
        serverUri,
        new AsynchronousHttpClientFactory()
          .createClient(httpClient)
          .asInstanceOf[DisposableHttpClient]
      )
    }
  }


}

class JiraClient(
  private val serverUri: URI,
  private val userName: String,
  private val password: String
) {
  /**
    *
    * @tparam T
    */
  abstract class Builder[T] {
    def build(): Try[T]
  }

  /**
    *
    */
  class IssueBuilder(
    project: BasicProject,
    issueType: IssueType
  ) extends Builder[Issue] {
    private val issueInputBuilder: IssueInputBuilder
    = new IssueInputBuilder(project, issueType)

    override def build(): Try[Issue] = {
      val issue: Promise[BasicIssue] = issueRestClient.createIssue(
        issueInputBuilder.build()
      )

      try {
        val basicIssue: BasicIssue = issue.claim()
        Success(issueRestClient.getIssue(basicIssue.getKey).claim())
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }
  }


  private val jiraRestClientFactory: JiraClient.JiraRestClientFactory
    = new JiraClient.JiraRestClientFactory

  private val jiraRestClient: JiraRestClient = jiraRestClientFactory
    .createWithBasicHttpAuthentication(
      serverUri,
      userName,
      password
    )

  private val componentRestClient: ComponentRestClient
    = jiraRestClient.getComponentClient

  private val issueRestClient: IssueRestClient
    = jiraRestClient.getIssueClient

  private val metadataRestClient: MetadataRestClient
    = jiraRestClient.getMetadataClient

  private val projectRestClient: ProjectRestClient
    = jiraRestClient.getProjectClient

  private val projectRolesRestClient: ProjectRolesRestClient
    = jiraRestClient.getProjectRolesRestClient

  private val userRestClient: UserRestClient
    = jiraRestClient.getUserClient
}
