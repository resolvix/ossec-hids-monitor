package com.resolvix.ohm.module.jira

import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Category, Location, Signature}
import com.resolvix.ohm.api.{AvailableModule, Module}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Alert, ModuleAlertStatus, NewStageAlert}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object JiraModule
  extends AvailableModule
{
  def doInstantiate(
    configuration: Map[String, Any]
  ): Module[_ <: Alert, _ <: ModuleAlertStatus] = {
    new JiraModule(configuration)
  }

  override def getDescriptor: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"
}

class JiraModule(
  configuration: Map[String, Any]
) extends AbstractModule[JiraModule, NewStageAlert, ModuleAlertStatus] {
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???

  override def getDescriptor: String = JiraModule.getDescriptor

  override def getHandle: String = JiraModule.getHandle

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
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

