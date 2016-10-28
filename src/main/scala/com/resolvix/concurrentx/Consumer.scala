package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.{Configuration, Pipe}

import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends Actor[C, P, V]
    with api.Consumer[C, P, V] {

  /**
    *
    */
  protected val packetPipe: PacketPipe[C, P, V] = new PacketPipe[C, P, V]()

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
  ): Try[Pipe.Producer[V]] = {
    try {
      packetPipe.
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
  def open: Try[Pipe.Consumer[V]] = {
    try {

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
