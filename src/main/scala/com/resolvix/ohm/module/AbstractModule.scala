package com.resolvix.ohm.module

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.ConsumerPipe
import com.resolvix.ohm.api.{Alert, Consumer, Module, ModuleAlertStatus, Producer}

import scala.concurrent._
import scala.util.{Failure, Success, Try}

object AbstractModule
{
  class ConsumerC[C]
    extends Consumer[C]
  {
    override protected def getSelf: Consumer[C] = this
  }

  class ProducerC[C]
    extends Producer[C]
  {
    override protected def getSelf: Producer[C] = this
  }

  class ConsumerP[P]
    extends Consumer[P]
  {
    override protected def getSelf: Consumer[P] = this
  }

  class ProducerP[P]
    extends Producer[P]
  {
    override protected def getSelf: Producer[P] = this
  }
}

import AbstractModule._

abstract class AbstractModule[C <: Alert]
  extends Module[
    ProducerC[C],
    ConsumerC[C],
    ProducerP[ModuleAlertStatus],
    ConsumerP[ModuleAlertStatus]
  ]
{
  def doConsume(c: C): Try[Boolean]

  def run(): Unit = {
    super.start()

    val consumerC: ConsumerC[C] = getConsumer

    val consumerPipe: ConsumerPipe[C] = consumerC.open match {
      case Success(consumerPipe: ConsumerPipe[C]) =>
        consumerPipe

      case Failure(t: Throwable) =>
        throw t
    }

    while (super.isRunning || super.isFinishing) {
      consumerPipe.read(5000, TimeUnit.MILLISECONDS) match {
        case Success(c: C) =>
          doConsume(c)

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
}
