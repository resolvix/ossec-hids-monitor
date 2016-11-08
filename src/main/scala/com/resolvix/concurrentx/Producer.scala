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
  P <: Producer[P, C, V],
  C <: Consumer[C, P, V],
  V
] extends Actor[P, C, V]
    with api.Producer[P, C, V]
{

  //
  //
  //
  protected var writerMap: Map[Int, Writer[V] /*MessageQueue[V]#Writer*/]
    = Map[Int, Writer[V]/*MessageQueue[V]#Writer*/]()

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
  override def open[R <: Reader[V]](
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
  def open[W <: Writer[V]]: Try[W] = {
    try {
      //writerMap

      val qq = actors.collect({
        case x: (Int, C) => {
          val q: Try[W] = x._2.open(getSelf)

          //if (q)

          val r = q match {
            case Success(w: Writer[V]) =>
              println(w)
              (x._1, w)

            case Failure(t: Throwable) =>
              throw t

            case _ =>
              throw new IllegalStateException()
          }

          r
        }
      })

      writerMap = qq.toMap[Int, Writer[V]]

      Success(new MulticastWriter[V](writerMap).asInstanceOf[W])
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
