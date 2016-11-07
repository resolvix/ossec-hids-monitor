package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.{Configuration, ConsumerNotRegisteredException}

import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.mq.MessageQueue

import scala.concurrent.duration.TimeUnit
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
  P <: Producer[P, C, R, V],
  C <: Consumer[C, P, Producer[P, C, R, V]#MulticastWriter, V],
  R <: MessageQueue[V]#Reader[C],
  V
] extends Actor[P, C, V]
    with api.Producer[P, Producer[P, C, R, V]#MulticastWriter, C, R, V]
{

  //
  //
  //
  protected var writerMap: Map[Int,MessageQueue[V]#Writer[P, C]]
    = Map[Int, MessageQueue[V]#Writer[P, C]]()

  /**
    *
    * @param consumer
    * @return
    */
  override def close(
    consumer: C
  ): Try[Boolean] = {
    /*super.close(consumer)*/
    Success(true)
  }

  /**
    *
    * @param consumer
    * @return
    */
  override def open(
    consumer: C
  ): Try[R] = {
    if (super.isRegistered(consumer)) {
      try {
        Success(
          consumer.open.get.asInstanceOf[R]
        )
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    } else {
      Failure(new ConsumerNotRegisteredException)
    }
  }

  /**
    *
    * @return
    */
  def open: Try[MulticastWriter] = {
    try {
      writerMap = actors.collect({
        case x: (Int, C) => {
          x._2.open(getSelf) match {
            case Success(producer: MessageQueue[V]#Writer[P, C]) =>
              (x._1, producer)

            case Failure(t: Throwable) =>
              throw t
          }
        }
      }).toMap

      Success(new MulticastWriter)
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
