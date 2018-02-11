package com.resolvix.ccs.impl

import com.resolvix.ccs.api
import com.resolvix.ccs.api.{Configuration, ConsumerNotRegisteredException}
import com.resolvix.mq.api.{Reader, Writer}

import scala.util.control.NonFatal
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

  /**
    *
    * @param writerMap
    */
  class MulticastWriter(
    writerMap: Map[Int, Writer[V]]
  ) extends com.resolvix.mq.api.Writer[V] {
    /**
      *
      * @param v
      * @return
      */
    override def write(
      v: V
    ): Try[Boolean] = {
      for ((y: Int, w: Writer[V]) <- writerMap) {
        w.write(v)
      }
      Success(true)
    }

    override def getId: Int = {
      throw new IllegalAccessException()
    }

    override def getSelf: MulticastWriter = this
  }

  //
  //
  //
  protected var writerMap: Map[Int, Writer[V]]
    = Map[Int, Writer[V]]()

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
        case NonFatal(t) =>
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
      writerMap = actors.collect({
        case x: (Int, api.Consumer[V]) => {
          x._2.open(getSelf) match {
            case Success(w: Writer[V]) =>
              println(w)
              (x._1, w)

            case Failure(t: Throwable) =>
              throw t

            case _ =>
              throw new IllegalStateException()
          }
        }
      })

      Success(new MulticastWriter(writerMap))
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
