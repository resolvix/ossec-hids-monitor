package com.resolvix.ohm.api

import com.resolvix.ohm.module.api.{Alert, ResultX}

import scala.util.Try

/**
  * The Module trait defines the basic intercom framework for receiving
  * alert objects from an alert producing actor, and for transmitting
  * module status updates to a module status update consuming actor.
  *
  * @tparam A
  *    refers to the type of alert to be consumed by the module
  *
  */
@deprecated("Deprecated in favour of the version under com.resolvix.ohm.module.api", "2017/03/17")
trait Module[A <: Alert, M <: ResultX]
  extends com.resolvix.ccs.runnable.api.ConsumerProducer[Module[A, M], A, M]
{
  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def finish(): Unit

  def initialise(): Try[Boolean]

  def run(): Unit

  def terminate(): Try[Boolean]
}
