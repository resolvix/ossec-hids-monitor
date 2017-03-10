package com.resolvix.ohm.module.jira

import java.io.{File, InputStream}
import java.net.URI
import java.time.format.DateTimeFormatter
import java.time.{Instant, OffsetDateTime, ZoneId}
import java.util.Date
import java.util.concurrent.TimeUnit

import com.atlassian.event.api.EventPublisher
import com.atlassian.httpclient.apache.httpcomponents.DefaultHttpClient
import com.atlassian.httpclient.api.{HttpClient, Request}
import com.atlassian.httpclient.api.factory.HttpClientOptions
import com.atlassian.httpclient.spi.ThreadLocalContextManager
import com.atlassian.jira.rest.client.api._
import com.atlassian.jira.rest.client.api.domain._
import com.atlassian.jira.rest.client.api.domain.input.{FieldInput, IssueInput, IssueInputBuilder}
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler
import com.atlassian.jira.rest.client.internal.async.{AsynchronousHttpClientFactory, AsynchronousJiraRestClient, AtlassianHttpClientDecorator, DisposableHttpClient}
import com.atlassian.sal.api.ApplicationProperties
import com.atlassian.util.concurrent.{Effect, Promise}
import com.sun.scenario.effect.Offset

import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._
import scala.util.control.NonFatal

object JiraSession {

  private final val DefaultTimeZoneId: ZoneId = ZoneId.systemDefault()

  //
  //  JIRA date / time fields are specified in the form -
  //
  //   "2017-03-10T00:16:00.000+0000"
  //
  //  For more details see the following link -
  //
  //    https://developer.atlassian.com/jiradev/jira-apis/jira-rest-apis/jira-rest-api-tutorials/jira-rest-api-example-create-issue#JIRARESTAPIExample-CreateIssue-Examplesofcreatinganissue
  //
  private final val JiraDateTimeFormatter: DateTimeFormatter
    = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SZ")

  /**
    *
    */
  object jiraRestClientFactory
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
        override def getDisplayName: String = "OHM JIRA Rest Client"
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

  class FieldMap(
    val fields: Iterable[Field]
  ) {
    val fieldByName: Map[String, Field] = fields.map {
      (f: Field) => (f.getName, f)
    }.toMap

    val fieldById: Map[String, Field] = fields.map {
      (f: Field) => (f.getId, f)
    }.toMap

    def getById(
      id: String
    ): Option[Field] = {
      fieldById.get(id)
    }

    def getByName(
      name: String
    ): Option[Field] = {
      fieldByName.get(name)
    }
  }
}

class JiraSession(
  private val serverUri: URI,
  private val userName: String,
  private val password: String
) {

  import JiraSession._

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

    def addField[T](
      field: Field,
      t: T
    ): IssueBuilder = {
      issueInputBuilder.setFieldInput(
        new FieldInput(field.getId, t)
      )
      this
    }

    def addField(
      field: Field,
      instant: Instant,
      zoneId: ZoneId
    ): IssueBuilder = {
      issueInputBuilder.setFieldInput(
        new FieldInput(
          field.getId,
          OffsetDateTime.ofInstant(instant, zoneId)
            .format(JiraDateTimeFormatter)
        )
      )
      this
    }

    def addField(
      field: Field,
      instant: Instant
    ): IssueBuilder = {
      addField(
        field,
        instant,
        DefaultTimeZoneId
      )
    }

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

    def setComponents(
      components: Iterable[BasicComponent]
    ): IssueBuilder = {
      issueInputBuilder.setComponents(components.asJava)
      this
    }

    def setDescription(
      description: String
    ): IssueBuilder = {
      issueInputBuilder.setDescription(description)
      this
    }

    def setPriority(
      priority: Priority
    ): IssueBuilder = {
      issueInputBuilder.setPriority(priority)
      this
    }

    def setSummary(
      summary: String
    ): IssueBuilder = {
      issueInputBuilder.setSummary(summary)
      this
    }
  }

  private val jiraRestClient: JiraRestClient
    = jiraRestClientFactory
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

  def addAttachment(
    issue: Issue,
    inputStream: InputStream,
    filename: String
  ): Try[Boolean] = {
    try {
      issueRestClient.addAttachment(
        issue.getSelf,
        inputStream,
        filename
      )
      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  def addAttachments(
    issue: Issue,
    files: File*
  ): Try[Boolean] = {
    try {
      for (file: File <- files) {
        issueRestClient.addAttachments(
          issue.getSelf,
          file
        )
      }
      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  def addComment(
    issue: Issue,
    comment: String
  ): Try[Boolean] = {
    try {
      issueRestClient.addComment(
        issue.getSelf(),
        Comment.valueOf(comment)
      )
      Success(true)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def close(): Try[Boolean] = {
    try {
      jiraRestClient.close()
      Success(true)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getComponent(
    uri: URI
  ): Try[Component] = {
    try {
      Success(
        componentRestClient.getComponent(uri).claim()
      )
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getIssue(
    issueId: String
  ): Try[Issue] = {
    try {
      Success(
          issueRestClient.getIssue(issueId).claim()
      )
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getIssueType(
    uri: URI
  ): Try[IssueType] = {
    try {
      Success(metadataRestClient.getIssueType(uri).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getIssueTypes(): Try[Iterable[IssueType]] = {
    try {
      val issueTypes: java.lang.Iterable[IssueType]
        = metadataRestClient.getIssueTypes().claim()
      Success(issueTypes.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getIssueTypes(
    project: Project
  ): Try[Iterable[IssueType]] = {
    try {
      val issueTypes: java.lang.Iterable[IssueType]
        = project.getIssueTypes
      Success(issueTypes.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getPriority(
    uri: URI
  ): Try[Priority] = {
    try {
      Success(metadataRestClient.getPriority(uri).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getPriorities: Try[Iterable[Priority]] = {
    try {
      val priorities: java.lang.Iterable[Priority]
        = metadataRestClient.getPriorities.claim()
      Success(priorities.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getProject(
    projectId: String
  ): Try[Project] = {
    try {
      Success(projectRestClient.getProject(projectId).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getProject(
    uri: URI
  ): Try[Project] = {
    try {
      Success(projectRestClient.getProject(uri).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getProjects: Try[Iterable[BasicProject]] = {
    try {
      val projects: java.lang.Iterable[BasicProject]
        = projectRestClient.getAllProjects.claim()
      Success(projects.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getRole(
    uri: URI
  ): Try[ProjectRole] = {
    try {
      Success(projectRolesRestClient.getRole(uri).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getRoles(
    project: Project
  ): Try[Iterable[ProjectRole]] = {
    try {
      val roles: java.lang.Iterable[ProjectRole]
        = projectRolesRestClient.getRoles(project.getSelf).claim()
      Success(roles.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getStatus(
    uri: URI
  ): Try[Status] = {
    try {
      Success(metadataRestClient.getStatus(uri).claim())
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getStatuses: Try[Iterable[Status]] = {
    try {
      val statuses: java.lang.Iterable[Status]
        = metadataRestClient.getStatuses.claim()
      Success(statuses.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def getVersions(
    project: Project
  ): Try[Iterable[Version]] = {
    try {
      Success(project.getVersions.asScala)
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

  def newIssue(
    project: Project,
    issueType: IssueType
  ): IssueBuilder = {
    new IssueBuilder(project, issueType)
  }
}
