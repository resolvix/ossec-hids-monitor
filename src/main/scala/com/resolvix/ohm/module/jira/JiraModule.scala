package com.resolvix.ohm.module.jira

import com.resolvix.ohm.{Category, Location, Signature}
import com.resolvix.ohm.api.{Alert, Module, ModuleAlertStatus}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.NewStageAlert

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

class JiraModule
  extends AbstractModule[NewStageAlert]
{
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def getDescriptor: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  override def getId: Int = ???

  override def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean] = {
    Success(false)
  }

  /*override def process(
    alert: Alert,
    location: Option[Location],
    signature: Option[Signature]
  ): Promise[ModuleAlertStatus] = {
    Promise()
  }*/

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}

