package com.resolvix.ohm.api

import scala.concurrent.Future
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
    alert: Alert
  ): Try[Future[ModuleAlertStatus]]

  def terminate(): Try[Boolean]

}
