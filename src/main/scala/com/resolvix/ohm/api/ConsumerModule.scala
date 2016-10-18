package com.resolvix.ohm.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Consumer, ConsumerProducer, Producer}
import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 08/10/16.
  */
trait ConsumerModule[C <: Alert, P <: ModuleAlertStatus]
  extends ConsumerProducer[C, P]
    with com.resolvix.concurrent.api.Runnable
{
  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  override def getConsumer: Consumer[C] = super.getConsumer

  override def getProducer: Producer[P] = super.getProducer

  def doConsume(c: C): Try[Boolean]

  def run(): Unit = {
    super.start()
    while (super.isRunning || super.isFinishing) {
      getConsumer.consume(5000, TimeUnit.MILLISECONDS) match {
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

  def terminate(): Try[Boolean]
}
