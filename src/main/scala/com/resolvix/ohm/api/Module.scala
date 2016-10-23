package com.resolvix.ohm.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.ConsumerProducer
import com.resolvix.concurrent.api.{ConsumerFactory, ProducerFactory}
import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

/**
  *
  * @tparam A
  */
trait Module[A <: Alert]
  extends ConsumerProducer[
    Producer[A],
    Consumer[A],
    A,
    Producer[ModuleAlertStatus],
    Consumer[ModuleAlertStatus],
    ModuleAlertStatus
  ] with com.resolvix.concurrent.api.Runnable
{

  class ConsumerFactoryCF
    extends ConsumerFactory[ConsumerFactoryCF, Consumer[A], Producer[A], A]
  {
    override def newInstance: ConsumerFactoryCF = ???
  }

  class ProducerFactoryPF
    extends ConsumerFactory[ProducerFactoryPF, Producer[ModuleAlertStatus], Consumer[ModuleAlertStatus], ModuleAlertStatus]
  {
    override def newInstance: ProducerFactoryPF = ???
  }

  val consumerFactory: ConsumerFactoryCF = new ConsumerFactoryCF

  val producerFactory: ProducerFactoryPF = new ProducerFactoryPF

  /**
    *
    * @return
    */
  override def getConsumerFactory[CF]: ConsumerFactoryCF = consumerFactory

  /**
    *
    * @return
    */
  override def getProducerFactory[PF]: ProducerFactoryPF = producerFactory

  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  def run(): Unit

  def terminate(): Try[Boolean]
}
