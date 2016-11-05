package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration
import com.resolvix.mq.api.{MessageQueue, Reader, Writer}

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
  P <: Producer[P, C, V],
  C <: Consumer[C, P, V],
  V
] extends Actor[P, C, V]
    with api.Producer[P, C, V]
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
      for ((y: Int, w: Writer[_, P, V]) <- producerMap) {
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
  protected var producerMap: Map[Int, Writer[_, P, V]]
    = Map[Int, Writer[_, P, V]]()

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
  ): Try[com.resolvix.mq.MessageQueue[V]#Reader[C]] = {
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
  def open: Try[Writer[_, P, V]] = {
    try {
      producerMap = actors.collect({
        case x: (Int, C) => {
          x._2.open(getSelf) match {
            case Success(producer: Writer[_, P, V]) =>
              (x._1, producer)

            case Failure(t: Throwable) =>
              throw t
          }
        }
      }).toMap

      Success(new WriterMQV)
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
