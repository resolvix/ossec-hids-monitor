package com.resolvix.ccs.impl

import com.resolvix.ccs.api
import com.resolvix.ccs.api.{Configuration, ProducerNotRegisteredException}
import com.resolvix.mq.MessageQueueFactory
import com.resolvix.mq.api.{MessageQueue, Reader, Writer}

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
  protected var messageQueue: MessageQueue[V] = _

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

  def getMessageQueue[V] = {
    messageQueue match {
      case mq: MessageQueue[V @unchecked] =>
        messageQueue

      case _ =>
        messageQueue = MessageQueueFactory.newMessageQueue()
        messageQueue
    }
  }

  /**
    *
    * @return
    */
  def getReader: Reader[V] = {
    getMessageQueue.getReader(getSelf)
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
    if (!super.isRegistered(producer))
      return Failure(new ProducerNotRegisteredException)
    Try({getMessageQueue.getWriter(producer, getSelf)})
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
