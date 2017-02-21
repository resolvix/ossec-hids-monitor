package com.resolvix.ohm.module.api

import scala.util.Try

/**
  * The Instance trait defines the basic intercom framework for receiving
  * alert objects from an alert producing actor, and for transmitting
  * module status updates to a module status update consuming actor.
  *
  * @tparam A
  *    refers to the type of alert to be consumed by the module
  *
  */
trait Instance[A <: Alert, M <: ModuleAlertStatus]
  extends com.resolvix.ccs.runnable.api.ConsumerProducer[Instance[A, M], A, M]
{
  /**
    *
    * @return
    */
  def getId: Int

  /**
    *
    * @return
    */
  def getModule: Module[A, M]

  /**
    *
    */
  def finish(): Unit

  /**
    *
    * @return
    */
  def initialise(): Try[Boolean]

  /**
    *
    */
  def run(): Unit

  /**
    *
    * @return
    */
  def terminate(): Try[Boolean]
}
