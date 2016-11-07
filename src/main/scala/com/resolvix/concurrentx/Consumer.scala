package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.{Configuration, ProducerNotRegisteredException}
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.mq.MessageQueue

import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

trait Consumer[
  C <: Consumer[C, P, W, V],
  P <: Producer[P, C, MessageQueue[V]#Reader[C], V],
  W <: MessageQueue[V]#Writer[P, C],
  V
] extends Actor[C, P, V]
    with api.Consumer[C, MessageQueue[V]#Reader[C], P, W, V]
{
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
    /*getReader.getWriter.close(producer) match {
      case Success(b: Boolean) =>
        Success(b)

      case Failure(t: Throwable) =>
        Failure(t)
    }*/
    Success(true)
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
    * @return
    */
  def getReader: MessageQueue[V]#Reader[C] = {
    //
    //  If a packet pipe for the Consumer does not already exist, create a
    //  new packet pipe for the Consumer.
    //
    messageQueueReader match {
      case r: MessageQueue[V]#Reader[C] =>
        messageQueueReader

      case _ =>
        val messageQueue = new MessageQueue[V]()
        this.messageQueueReader = messageQueue.getReader(getSelf)
        this.messageQueueReader
    }
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
  ): Try[MessageQueue[V]#Writer[P, C]] = {
    if (super.isRegistered(producer)) {
      try {
        getReader.getWriter(producer)
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    } else {
      Failure(new ProducerNotRegisteredException)
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
