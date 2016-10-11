package com.resolvix.ohm.module.jira

import com.resolvix.ohm.{Category, Location, Signature, api}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

class JiraModule
  extends api.Module
{
  override def getDescriptor: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  override def getId: Int = ???

  override def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean] = {
    Success(false)
  }

  override def process(
    alert: Alert,
    location: Option[Location],
    signature: Option[Signature]
  ): Promise[ModuleAlertStatus] = {
    Promise()
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}

