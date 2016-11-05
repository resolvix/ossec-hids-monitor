package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration
import com.resolvix.mq.api.MessageQueue

import scala.util.{Failure, Success, Try}

/**
  *
  * @tparam C
  *   refers to the type of the consumer
  *
  * @tparam P
  *   refers to the type of the producer
  *
  * @tparam V
  *   specifies the class of values to be passed between the local actor
  *   and the remote actors
  */
trait Producer[
  P <: Producer[P, C, V],
  C <: Consumer[C, P, V],
  V
] extends Actor[P, C, V]
    with api.Producer[P, C, V]
{
  //
  //
  //
  protected var producerMap: Map[Int, MessageQueue[V]#Producer[P, C]]
    = Map[Int, MessageQueue[V]#Producer[P, C]]()

  /**
    *
    * @param consumer
    * @return
    */
  override def close(
    consumer: C
  ): Try[Boolean] = {
    super.close(consumer)
  }

  /**
    *
    * @param consumer
    * @return
    */
  override def open(
    consumer: C
  ): Try[MessageQueue[V]#Consumer[C, P]] = {
    try {
      Success(
        consumer.open.get
      )
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @return
    */
  def open: Try[MessageQueue[V]#Producer[P, C]] = {
    try {
      producerMap = actors.collect({
        case x: (Int, C) => {
          x._2.open(getSelf) match {
            case Success(producerPipe: api.ProducerPipe[V]) =>
              (x._1, producerPipe)

            case Failure(t: Throwable) =>
              throw t
          }
        }
      })

      Success(
        /*new ProducerPipe[P, C, V](
          getSelf,
          pack[etPipes
        )*/
      )
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
    * @return
    */
  override def register(
    consumer: C
  ): Try[Boolean] = {
    super.register(consumer)
  }

  /**
    *
    * @return
    */
  override def unregister(
    consumer: C
  ): Try[Boolean] = {
    super.unregister(consumer)
  }
}
