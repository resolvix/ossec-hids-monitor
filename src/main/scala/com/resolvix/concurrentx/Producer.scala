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
trait Producer[V]
  extends Actor[api.Producer[V], api.Consumer[V], V]
    with api.Producer[V]
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
    consumer: api.Consumer[V]
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
    consumer: api.Consumer[V]
  ): Try[Reader[V]] = {
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
  def open: Try[Writer[V]] = {
    try {
      //writerMap

      val qq = actors.collect({
        case x: (Int, api.Consumer[V]) => {
          val q: Try[Writer[V]] = x._2.open(getSelf)

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

      Success(new MulticastWriter[V](writerMap))
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
    consumer: api.Consumer[V]
  ): Try[Boolean] = {
    super.register(consumer)
  }

  /**
    *
    * @return
    */
  override def unregister(
    consumer: api.Consumer[V]
  ): Try[Boolean] = {
    super.unregister(consumer)
  }
}
