package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration, Consumer, Producer}

import scala.util.{Success, Try}

/**
  *
  */
object ConsumerProducer {



}

/**
  *
  * @tparam T
  */
trait ConsumerProducer[C, P] {

  /**
    *
    */
  class LocalConsumer
    extends AbstractConsumer[LocalConsumer, LocalProducer, C]
  {
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

  /**
    *
    */
  class LocalProducer
    extends AbstractProducer[LocalProducer, LocalConsumer, P]
  {
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

  /**
    *
    */
  private val consumer: LocalConsumer
    = new LocalConsumer()

  /**
    *
    */
  private val producer: LocalProducer
    = new LocalProducer()

  /**
    *
    * @return
    */
  def getConsumer: Consumer[LocalConsumer, LocalProducer, C] = this.consumer

  /**
    *
    * @return
    */
  def getProducer: Producer[LocalProducer, LocalConsumer, P] = this.producer


  def consume: Try[C] = {
    consumer.consume
  }

  def consume(
    timeout: Int,
    unit: TimeUnit
  ): Try[C] = {
    consumer.consume(
      timeout,
      unit
    )
  }

  def produce(
    p: P
  ): Try[Boolean] = {
    producer.produce(p)
  }

}
