package com.resolvix.ohm.module.jira

import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Category, Location, Signature}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Module, Instance, NewStageAlert}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object JiraModule
  extends AbstractModule[NewStageAlert, ModuleAlertStatus]
  with Module[NewStageAlert, ModuleAlertStatus]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  protected override def newInstance(
    configuration: Map[String, Any]
  ): Try[Instance[NewStageAlert, ModuleAlertStatus]] = {
    Success(
      new JiraModule(configuration)
    )
  }
}

class JiraModule(
  configuration: Map[String, Any]
) extends AbstractModule[NewStageAlert, ModuleAlertStatus]#AbstractInstance[JiraModule]
  with Instance[NewStageAlert, ModuleAlertStatus]
{
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???

  override def getModule: Module[NewStageAlert, ModuleAlertStatus] = JiraModule

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

