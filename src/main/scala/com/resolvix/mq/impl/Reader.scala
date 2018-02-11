package com.resolvix.mq.impl

import com.resolvix.mq.api

import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * Objects of type Reader[C] are intended to provide an interface
  * between the ultimate consumer and the message stream for messages
  * of type V.
  *
  * The Reader class enables the consumer to ascertain which
  * counterparty the message came from.
  *
  * @tparam R
  *   specifies the type of the identifier for Consumer objects intended
  *   to consume messages of type V
  *
  * @tparam W
  */
class Reader[V](
  private val messageQueue: MessageQueue[V],
  private val consumer: Any
) extends Actor
  with api.Reader[V] {

  //
  //  Obtain a Reader object for the underlying message stream.
  //
  val messageConsumer: MessageStream[V]#Reader
  = MessageQueue.getMessageStream[V](getId).getReader


  /**
    *
    * @return
    */
  override def getSelf: Reader[V] = this

  /**
    *
    * @param producer
    * @return
    */
  def getWriter(
    producer: Any
  ): Try[Writer[V]] = {
    Success(
      messageQueue.getWriter(
        producer,
        consumer
      )
    )
  }

  /**
    *
    * @param consumer
    * @return
    */
  def hasConsumer(consumer: Any): Boolean = {
    this.consumer.equals(consumer)
  }

  /**
    *
    * @return
    */
  def read: Try[(Int, V)] = {
    messageConsumer.read match {
      case Success(p: Packet[V]) =>
        Success(
          (p.getWriter, p.getV)
        )

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[(Int, V)] = {
    messageConsumer.read(timeout, unit) match {
      case Success(p: Packet[V]) =>
        Success(
          (p.getWriter, p.getV)
        )

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }
}
