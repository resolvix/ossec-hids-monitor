package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.Configuration

import scala.concurrent.duration.{TimeUnit, _}
import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends Actor[C, ProducerPipe[P, C, V], P, ConsumerPipe[C, P, V], V]
    with api.Consumer[C, ProducerPipe[C, P, V], P, ConsumerPipe[C, P, V], V] {

  /**
    *
    * @param producer
    * @return
    */
  override def close(
    producer: P
  ): Try[Boolean] = {
    super.close(producer) match {
      case Success(b: Boolean) =>
        Success(b)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  /**
    *
    * @return
    */
  protected def close: Try[Boolean] = {
    Success(true)
  }

  protected def newConsumerPipe(
    consumer: C
  ): Try[ConsumerPipe[C, P, V]]

  protected def newProducerPipe(
    producer: P
  ): Try[ProducerPipe[P, C, V]]

  /**
    * The open method, with a parameter of derivative type Producer, is
    * intended to provide the calling producer with a pipe suitable for
    * the transmission of values of type V, by the producer, to the instant
    * consumer.
    *
    * @param producer
    *    the producer
    *
    * @return
    */
  override def open(
    producer: P
  ): Try[ProducerPipe[P, C, V]] = {
    try {
      newProducerPipe(producer)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    * The open method, specified without a parameter, is intended to
    * provide the calling consumer with a pipe suitable for the receipt
    * of values of type V, by the consumer, from the producers.
    *
    * @return
    */
  def openX: Try[api.ConsumerPipe[V]] = {
    try {
      newConsumerPipe(this.asInstanceOf[C])
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @param configuration
    * @return
    */
  def initialise(
    configuration: Configuration
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  override def register(
    producer: P
  ): Try[Boolean] = {
    super.register(producer)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def unregister(
    producer: P
  ): Try[Boolean] = {
    super.unregister(producer)
  }
}
