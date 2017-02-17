package com.resolvix.ohm.api

import com.resolvix.ccs.runnable.ConsumerProducer

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
trait Module[A <: Alert, M <: ModuleAlertStatus]
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
