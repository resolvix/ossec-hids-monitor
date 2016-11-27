package com.resolvix.ccs

import com.resolvix.ccs.api.{Configuration, ProducerNotRegisteredException}
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.mq.MessageQueue

import scala.concurrent.duration.TimeUnit
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

trait Consumer[V]
  extends Actor[api.Consumer[V], api.Producer[V], V]
    with api.Consumer[V]
{
  /**
    *
    */
  @transient
  protected var messageQueueReader: MessageQueue[V]#Reader = _

  /**
    *
    * @param producer
    * @return
    */
  override def close(
    producer: api.Producer[V]
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
  def getReader: MessageQueue[V]#Reader = {
    //
    //  If a packet pipe for the Consumer does not already exist, create a
    //  new packet pipe for the Consumer.
    //
    messageQueueReader match {
      case r: MessageQueue[V]#Reader =>
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
    producer: api.Producer[V]
  ): Try[Writer[V]] = {
    if (super.isRegistered(producer)) {
      try {
        getReader.getWriter(producer)
      } catch {
        case NonFatal(t) =>
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
  def open: Try[Reader[V]] = {
    try {
      Success(getReader)
    } catch {
      case NonFatal(t) =>
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
  ): Try[Boolean] = {
    Success(true)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def register(
    producer: api.Producer[V]
  ): Try[Boolean] = {
    super.register(producer)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def unregister(
    producer: api.Producer[V]
  ): Try[Boolean] = {
    super.unregister(producer)
  }
}
