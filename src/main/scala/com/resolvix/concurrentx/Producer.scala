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
  P <: Producer[P, W, C, R, V],
  W <: Producer[P, W, C, R, V]#WriterMQV,
  C <: Consumer[C, R, P, W, V],
  R <: MessageQueue[V]#Reader[C],
  V
] extends Actor[P, C, V]
    with api.Producer[P, W, C, R, V]
{
  class WriterMQV
    extends Writer[WriterMQV, P, V]
  {
    /**
      *
      * @param v
      * @return
      */
    override def write(
      v: V
    ): Try[Boolean] = {
      for ((y: Int, w: Writer[_, P, V]) <- writerMap) {
        w.write(v)
      }
      Success(true)
    }

    override def getId: Int = {
      throw new IllegalAccessException()
    }

    def getSelf: WriterMQV = this
  }

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
    super.close(consumer)
  }

  /**
    *
    * @param consumer
    * @return
    */
  override def open(
    consumer: C
  ): Try[MessageQueue[V]#Reader[C]] = {
    if (super.isRegistered(consumer)) {
      try {
        Success(
          consumer.open.get
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
  def open: Try[W] = {
    try {
      writerMap = actors.collect({
        case x: (Int, C) => {
          x._2.open(getSelf) match {
            case Success(producer: Writer[_, P, V]) =>
              (x._1, producer)

            case Failure(t: Throwable) =>
              throw t
          }
        }
      }).toMap

      Success((new WriterMQV).asInstanceOf[W])
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
