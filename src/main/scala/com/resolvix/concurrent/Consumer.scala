package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.Configuration

import scala.concurrent.duration.{TimeUnit, _}
import scala.util.{Failure, Success, Try}

object Consumer
{

}

trait Consumer[
  C <: api.Consumer[C, P, _, V],
  P <: api.Producer[P, C, _, V],
  V
] extends Actor[C, P, api.ConsumerPipe[V], V]
    with api.Consumer[C, P, api.ConsumerPipe[V], V] {

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
        new Packet[P, C, V](producer, v)
      )
    }
  }

  sealed class LocalConsumerPipe
    extends ConsumerPipe[C, P, api.ConsumerPipe[V], V](packetPipe)
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
    *
    * @param producer
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
    *
    * @return
    */
  def open: Try[api.ConsumerPipe[V]] = {
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
