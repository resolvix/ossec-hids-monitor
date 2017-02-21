package com.resolvix.ohm.module.text

import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Category, Location, Signature, api}
import com.resolvix.ohm.api.{AvailableModule, Module}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Alert, ModuleAlertStatus, NewStageAlert}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

object TextModule
  extends AvailableModule
{
  def doInstantiate(
    configuration: Map[String, Any]
  ): Module[_ <: Alert, _ <: ModuleAlertStatus] = {
    new TextModule(configuration)
  }

  override def getDescriptor: String = "Module for rendering OSSEC HIDS alerts to a text-based report."

  override def getHandle: String = "TEXT"
}

class TextModule(
  configuration: Map[String, Any]
) extends AbstractModule[TextModule, NewStageAlert, ModuleAlertStatus] {
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???

  def getDescriptor: String = TextModule.getDescriptor

  def getHandle: String = TextModule.getHandle

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

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
