package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.mq.{Message, MessageQueue}

import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends Actor[C, P, V]
    with api.Consumer[C, P, V] {

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
  protected var messageQueueReader: MessageQueue[V]#Reader[C] = _

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
    * The open method, with a parameter of type P, is intended to provide
    * the caller with a Reader suitable for the receipt of values of type V,
    * sent by the producer, by the instant consumer.
    *
    * @param producer
    *    the producer
    *
    * @return
    *    an object providing the caller with a Reader object.
    */
  override def open(
    producer: P
  ): Try[MessageQueue[V]#Writer[P]] = {
    try {
      //
      //  If a packet pipe for the Consumer does not already exist, create a
      //  new packet pipe for the Consumer.
      //
      if (!messageQueueReader.isInstanceOf[Reader[_, C, V]]) {
        val messageQueue = new MessageQueue[V]()
        messageQueueReader = messageQueue.getReader(getSelf)
      }

      //
      //  Obtain a producer
      //
      messageQueueReader.getWriter(
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
  def open: Try[MessageQueue[V]#Reader[C]] = {
    try {
      Success(messageQueueReader)
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
