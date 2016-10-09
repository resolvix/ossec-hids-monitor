package com.resolvix.ohm.api

import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * Created by rwbisson on 08/10/16.
  */
trait Module {

  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  def process(
    alert: Alert,
    location: Option[Location],
    signature: Option[Signature]
  )(
    implicit ec: ExecutionContext
  ): Try[Future[ModuleAlertStatus]]

  def terminate(): Try[Boolean]

}
