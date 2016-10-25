package com.resolvix.ohm.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.ConsumerProducer
import com.resolvix.concurrent.api
import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

object Module
{
  /**
    *
    */
  class ConsumerFactory[A]
    extends api.ConsumerFactory[
      ConsumerFactory[A],
      Consumer[A],
      Producer[A],
      A
    ]
  {
    override def newInstance: Consumer[A] = ???
  }

  /**
    *
    */
  class ProducerFactory
    extends api.ConsumerFactory[
      ProducerFactory,
      Producer[ModuleAlertStatus],
      Consumer[ModuleAlertStatus],
      ModuleAlertStatus
    ]
  {
    override def newInstance: Producer[ModuleAlertStatus] = ???
  }
}

/**
  * The Module trait defines the basic intercom framework for receiving
  * alert objects from an alert producing actor, and for transmitting
  * module status updates to a module status update consuming actor.
  *
  * @tparam A
  *    refers to the type of alert to be consumed by the module
  *
  */
trait Module[A <: Alert]
  extends ConsumerProducer[
    Module.ConsumerFactory[A],
    Producer[A],
    Consumer[A],
    A,
    Module.ProducerFactory,
    Producer[ModuleAlertStatus],
    Consumer[ModuleAlertStatus],
    ModuleAlertStatus
  ] with com.resolvix.concurrent.api.Runnable
{
  val consumerFactory: Module.ConsumerFactory[A] = new Module.ConsumerFactory()

  val producerFactory: Module.ProducerFactory = new Module.ProducerFactory()

  /**
    *
    * @return
    */
  override def getConsumerFactory: Module.ConsumerFactory[A] = consumerFactory

  /**
    *
    * @return
    */
  override def getProducerFactory: Module.ProducerFactory = producerFactory

  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  def run(): Unit

  def terminate(): Try[Boolean]
}
