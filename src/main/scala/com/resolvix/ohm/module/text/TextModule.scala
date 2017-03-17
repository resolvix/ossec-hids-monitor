package com.resolvix.ohm.module.text

import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Category, Location, Signature, api}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Instance, Module, ModuleAlertStatus, NewStageAlert}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

object TextModule
  extends AbstractModule[NewStageAlert, ModuleAlertStatus]
  with Module[NewStageAlert, ModuleAlertStatus]
{

  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a text-based report."

  override def getHandle: String = "TEXT"

  override protected def newInstance(
    configuration: Map[String, Any]
  ): Try[TextModule] = {
    Success(
      new TextModule(configuration)
    )
  }


}

class TextModule(
  configuration: Map[String, Any]
) extends AbstractModule.AbstractInstance[TextModule, NewStageAlert, ModuleAlertStatus]
  with Instance[NewStageAlert, ModuleAlertStatus]
{
  /*override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???*/


  /**
    *
    */
  override def finish(): Unit = ???

  override def getModule: Module[NewStageAlert, ModuleAlertStatus]
    = TextModule

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(true)
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

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
