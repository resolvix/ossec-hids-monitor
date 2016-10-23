package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.Configuration

import scala.concurrent.duration.{TimeUnit, _}
import scala.util.{Failure, Success, Try}

object Consumer
{

}

trait Consumer[C <: Consumer[C, P, T], P <: Producer[P, C, T], T]
  extends Actor[C, P, T]
    with api.Consumer[C, P, T] {

  /**
    *
    * @param producer
    */
  sealed class LocalProducerPipe(
    producer: P
  ) extends api.ProducerPipe[T]
    with api.Pipe[T] {

    /**
      *
      * @param t
      * @return
      */
    def write(
      t: T
    ): Try[Boolean] = {
      packetPipe.write(
        new Packet[P, C, T](producer, t)
      )
    }
  }

  sealed class LocalConsumerPipe
    extends ConsumerPipe[C, P, T](packetPipe)
      with api.ConsumerPipe[T]
      with api.Pipe[T]

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

  /**
    *
    * @param producer
    * @return
    */
  override def open(
    producer: P
  ): Try[api.ProducerPipe[T]] = {
    try {
      Success(new LocalProducerPipe(producer))
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @return
    */
  def open: Try[api.ConsumerPipe[T]] = {
    try {
      Success(new LocalConsumerPipe)
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
