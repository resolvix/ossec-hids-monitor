package com.resolvix.ohm.module

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.resolvix.ohm.api.{Alert, Consumer, ModuleAlertStatus, Producer}
import com.typesafe.config.{Config, ConfigValue}

import scala.concurrent._
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object AbstractModule
{

  /**
    *
    * @tparam AI
    * @tparam A
    *    refers to the type of alert to be consumed by the module
    *
    * @tparam M
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, A, M], A <: Alert, M <: ModuleAlertStatus]
    extends com.resolvix.ohm.module.api.Instance[A, M]
      with com.resolvix.ccs.runnable.api.ConsumerProducer[AI, A, M]
  {
    /**
      *
      * @param C
      * @return
      */
    def doConsume(c: A): Try[Boolean]

    /**
      *
      */
    /*def run(): Unit = {
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
    }*/

    /**
      *
      */
    def finish(): Unit = {

    }
  }
}

/**
  *
  */
abstract class AbstractModule[A <: Alert, M <: ModuleAlertStatus]
  extends api.Module[A, M]
{
  import AbstractModule._
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

  /**
    *
    * @return
    */
  protected def getConfigurations: Array[String]

  /**
    *
    * @return
    */
  override def getDescription: String = ???

  /**
    *
    * @return
    */
  override def getHandle: String = ???

  /**
    *
    * @return
    */
  override def getId: Int = ???

  /**
    *
    * @param config
    * @return
    */
  protected def newInstance(
    config: Map[String, Any]
  ): Try[Instance[A, M]] = ???

  /**
    *
    * @return
    */
  override def getInstance(): Try[Instance[A, M]] = {
    newInstance(Map[String, Any]())
  }

  /**
    *
    * @param config
    * @return
    */
  override def getInstance(
    config: Config
  ): Try[Instance[A, M]] = {
    newInstance(
      getConfigurations.map(
        (h: String) => config.getValue(h) match {
          case v: ConfigValue =>
            (h, v.unwrapped())
        }
      ).toMap
    )
  }

  /**
    *
    * @param properties
    * @return
    */
  override def getInstance(
    properties: Properties
  ): Try[Instance[A, M]] = {
    newInstance(
      getConfigurations.map(
        (h: String) => properties.get(h) match {
          case a: Any =>
            (h, a)
        }
      ).toMap
    )
  }

  /**
    *
    * @return
    */
  override def getName: String = ???
}
