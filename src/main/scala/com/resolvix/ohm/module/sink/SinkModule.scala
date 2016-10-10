package com.resolvix.ohm.module.sink

import com.resolvix.ohm.{Location, Signature, api}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkModule
  extends api.Module
{
  override def getDescriptor: String = "Module for sinking OSSEC HIDS alerts."

  override def getHandle: String = "LOOPBACK"

  override def getId: Int = ???

  override def initialise(configuration: Map[String, Any]): Try[Boolean] = {
    Success(false)
  }

  override def process(alert: Alert, location: Option[Location], signature: Option[Signature])(implicit ec: ExecutionContext): Try[Future[ModuleAlertStatus]] = {
    Failure(???)
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
