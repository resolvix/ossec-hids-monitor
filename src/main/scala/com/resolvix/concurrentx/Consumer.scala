package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration
import com.resolvix.mq.{Message, MessageQueue}

import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends Actor[C, P, Message[P, C, V]]
    with api.Consumer[C, P, Message[P, C, V]] {

  /*class ProducerPipe(
    @transient
    val producer: P,

    @transient
    val producerPipe: api.ProducerPipe[Packet[P, C, V]]
  ) extends api.ProducerPipe[V] {

    /**
      *
      * @param v
      * @return
      */
    override def write(
      v: V
    ): Try[Boolean] = {
      val pV = new Packet[P, C, V](producer, v)
      producerPipe.write(pV)
    }
  }*/

  /**
    *
    */
  @transient
  protected var consumerPipe: MessageQueue[V]#Consumer[C, P] = _

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
  ): Try[MessageQueue[V]#Producer[P, C]] = {
    try {
      //
      //  If a packet pipe for the Consumer does not already exist, create a
      //  new packet pipe for the Consumer.
      //
      if (!consumerPipe.isInstanceOf[MessageQueue[V]#Consumer[C, P]]) {
        val packetPipe = new MessageQueue[V]()
        consumerPipe = packetPipe.getConsumer(getSelf)
      }

      //
      //  Obtain a produce
      //
      consumerPipe.getProducer(
        producer
      )
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
  def open: Try[MessageQueue[V]#Consumer[C, P]] = {
    try {
      Success(consumerPipe)
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
