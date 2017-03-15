package com.resolvix.ohm.module.jira

import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Instance, Module, NewStageAlert}

import scala.util.{Success, Try}

object JiraModule
  extends AbstractModule[JiraModule, NewStageAlert, ModuleAlertStatus]
  with Module[JiraModule, NewStageAlert, ModuleAlertStatus]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  /*protected override def newInstance(
    configuration: Map[String, Any]
  ): Try[JiraModule] = {
    Success(
      new JiraModule(configuration)
    )
  }*/

  override def newInstance(
    config: Map[String, Any]
  ): Try[Instance[JiraModule, NewStageAlert, ModuleAlertStatus]] = {
    Success(
      new JiraModule(config)
    )
  }
}

class JiraModule(
  configuration: Map[String, Any]
) extends AbstractModule.AbstractInstance[JiraModule, NewStageAlert, ModuleAlertStatus]
  with Instance[JiraModule, NewStageAlert, ModuleAlertStatus]
{
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???

  override def getModule: Module[JiraModule, NewStageAlert, ModuleAlertStatus] = JiraModule

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


  /**
    *
    */
  override def run(): Unit = ???

  /**
    *
    * @return
    */
  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}

