package com.resolvix.ohm.module

import java.util.concurrent.TimeUnit

import com.resolvix.ccs.runnable.ConsumerProducer
import com.resolvix.ohm.api.{Alert, Consumer, Module, ModuleAlertStatus, Producer}

import scala.concurrent._
import scala.util.{Failure, Success, Try}

object AbstractModule
{
  /*class ConsumerC[C]
    extends Consumer[C]
  {
    override protected def getSelf: Consumer[C] = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???
  }

  class ProducerC[C]
    extends Producer[C]
  {
    override protected def getSelf: Producer[C] = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???
  }

  class ConsumerP[P]
    extends Consumer[P]
  {
    override protected def getSelf: Consumer[P] = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???
  }

  class ProducerP[P]
    extends Producer[P]
  {
    override protected def getSelf: Producer[P] = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???
  }*/
}

abstract class AbstractModule[AM <: AbstractModule[AM, A, M], A <: Alert, M <: ModuleAlertStatus]
  extends Module[A, M]
  with ConsumerProducer[AM, A, M]
{
  import AbstractModule._

  def doConsume(c: A): Try[Boolean]

  def run(): Unit = {
    val consumerC: Consumer[A] = getConsumer

    val consumerPipe: ConsumerPipe[A] = consumerC.openX match {
      case Success(consumerPipe: ConsumerPipe[A]) =>
        consumerPipe

      case Failure(t: Throwable) =>
        throw t
    }

    while (super.isRunning || super.isFinishing) {
      consumerPipe.read(5000, TimeUnit.MILLISECONDS) match {
        case Success(a: A @unchecked) =>
          doConsume(a)

        case Failure(e: TimeoutException) =>
          //
          //  Do nothing
          //
          if (super.isFinishing) {
            finished()
          }

        case Failure(t: Throwable) =>
          throw t
      }
    }
  }

  def finish(): Unit = {

  }
}
