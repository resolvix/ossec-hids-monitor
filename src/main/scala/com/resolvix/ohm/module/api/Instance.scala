package com.resolvix.ohm.module.api

import scala.util.Try

/**
  * Defines the intercom framework for receiving alert objects from an alert
  * producing actor, and for transmitting module status updates to a module
  * status update consuming actor.
  *
  */
trait Instance[A <: Alert, R <: Result]
{

  /**
    *
    * @return
    */
  def getId: Int

  /**
    * Returns the parent 'Module' for the module instance.
    *
    * @return
    */
  def getModule: Module[A, R]

  /**
    * A method to
    *
    */
  def finish(): Unit

  /**
    *
    * @return
    */
  def initialise(): Try[Boolean]

  /**
    * Instantiate the consumer for the module.
    *
    */
  def run(): Unit

  /**
    * Terminate the consumer for the module.
    *
    * @return
    */
  def terminate(): Try[Boolean]
}
