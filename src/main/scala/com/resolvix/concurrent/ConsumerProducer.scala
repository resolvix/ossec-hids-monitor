package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration}

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

/**
  *
  */
object ConsumerProducer {

}

/**
  *
  * @tparam C
  * @tparam P
  */
trait ConsumerProducer[PC <: api.Producer[PC, _, C], C, CP <: api.Consumer[CP, _, P], P]
{

  /**
    *
    */
  sealed class CaptiveConsumerC
    extends Consumer[CaptiveConsumerC, PC, C]
  {

    override protected def getSelf: CaptiveConsumerC = this

    /**
      *
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
      Success(true)
    }
  }

  /*abstract class CaptiveProducerC
    extends Producer[CaptiveProducerC, CaptiveConsumerC, C]
  {
    override protected def getSelf: CaptiveProducerC = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???
  }*/

  /*sealed class CaptiveConsumerPipe(
    consumer: Consumer[CaptiveConsumer, CaptiveProducer, C]
  ) {
    val consumerPipe: ConsumerPipe[C] = consumer.open match {
      case Success(consumerPipe: ConsumerPipe[C]) =>
        consumerPipe

      case Failure(t: Throwable) =>
        throw t
    }

    /**
      *
      * @return
      */
    def read: Try[C] = {
      consumerPipe.read
    }

    /**
      *
      * @param timeout
      * @param unit
      * @return
      */
    def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[C] = {
      consumerPipe.read(timeout, unit)
    }
  }*/

  /**
    *
    */
  sealed class CaptiveProducerP
    extends Producer[CaptiveProducerP, CP, P]
  {

    override protected def getSelf: CaptiveProducerP = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
      Success(true)
    }
  }

  abstract class CaptiveConsumerP
    extends Producer[CaptiveProducerP, CaptiveConsumerP, P]

  /*sealed class CaptiveProducerPipe(
    producer: Producer[CaptiveProducer, CaptiveConsumer, P]
  ) {
    val producerPipe: ProducerPipe[P] = producer.open match {

    }

    /**
      *
      * @param p
      * @return
      */
    def write(
      p: P
    ): Try[Boolean]
  }*/

  /**
    *
    */
  private val consumer: CaptiveConsumerC
    = new CaptiveConsumerC()

  /**
    *
    */
  private val producer: CaptiveProducerP
    = new CaptiveProducerP()

  /**
    *
    * @return
    */
  def getConsumer: CaptiveConsumerC = this.consumer

  /**
    *
    * @return
    */
  def getProducer: CaptiveProducerP = this.producer
}
