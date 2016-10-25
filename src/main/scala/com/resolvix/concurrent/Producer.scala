package com.resolvix.concurrent

import com.resolvix.concurrent.api.{Configuration}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 19/10/16.
  */
trait Producer[P <: api.Producer[P, C, _, V], C <: api.Consumer[C, P, _, V], V]
  extends Actor[P, C, api.ProducerPipe[V], V]
    with api.Producer[P, C, api.ProducerPipe[V], V]
{
  //
  //
  //
  protected var consumerPacketPipes: Map[Int, api.ProducerPipe[V]]
    = Map[Int, api.ProducerPipe[V]]()

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

  protected def newConsumerPipe(
    consumer: C
  ): Try[api.ConsumerPipe[V]]

  protected def newProducerPipe(
    producer: P
  ): Try[api.ProducerPipe[V]]

  override def open(
    consumer: C
  ): Try[api.ConsumerPipe[V]] = {
    try {
      consumer.open
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @return
    */
  def open: Try[api.ProducerPipe[V]] = {
    try {
      consumerPacketPipes = actors.collect({
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
        new ProducerPipe[P, C, V](
          getSelf,
          consumerPacketPipes
        )
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
