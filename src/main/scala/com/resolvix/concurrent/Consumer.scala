package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.Configuration

import scala.concurrent.duration.{TimeUnit, _}
import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: api.Consumer[C, CT, P, PT, V],
  CT <: api.Transport[V],
  P <: api.Producer[P, PT, C, CT, V],
  PT <: api.Transport[V],
  V
] extends Actor[C, CT, P, PT, V]
    with api.Consumer[C, CT, P, PT, V] {

  /**
    *
    * @param producer
    */
  sealed class LocalProducerPipe(
    producer: P
  ) extends api.ProducerPipe[V]
    with api.Pipe[V] {

    /**
      *
      * @param v
      * @return
      */
    def write(
      v: V
    ): Try[Boolean] = {
      packetPipe.write(
        new Packet[P, PT, C, CT, V](producer, v)
      )
    }
  }

  sealed class LocalConsumerPipe
    extends ConsumerPipe[C, CT, P, PT, V](packetPipe)
      with api.ConsumerPipe[V]
      with api.Pipe[V]

  /**
    *
    * @param producer
    * @return
    */
  override def close(
    producer: P
  ): Try[Boolean] = {
    super.close(producer)
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
  ): Try[api.ConsumerPipe[V]]

  protected def newProducerPipe(
    producer: P
  ): Try[api.ProducerPipe[V]]

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
  ): Try[api.ProducerPipe[V]] = {
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
