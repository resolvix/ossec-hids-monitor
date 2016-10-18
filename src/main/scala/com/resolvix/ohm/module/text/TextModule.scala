package com.resolvix.ohm.module.text

import com.resolvix.ohm.{Category, Location, Signature, api}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}
import com.resolvix.ohm.module.api.NewStageAlert

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

class TextModule[C]
  extends api.ConsumerModule[NewStageAlert, ModuleAlertStatus]
{
  override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def getDescriptor: String = "Module for rendering OSSEC HIDS alerts to a text-based report."

  override def getHandle: String = "TEXT"

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
