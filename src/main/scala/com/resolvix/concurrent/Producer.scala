package com.resolvix.concurrent

import com.resolvix.concurrent.api.{Configuration}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

object Producer {

}

/**
  * Created by rwbisson on 19/10/16.
  */
trait Producer[P <: Producer[P, C, T], C <: Consumer[C, P, T], T]
  extends Actor[P, C, T]
    with api.Producer[P, C, T]
{
  //
  //
  //
  protected var consumerPacketPipes: Map[Int, api.ProducerPipe[T]]
    = Map[Int, api.ProducerPipe[T]]()

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

  override def open(
    consumer: C
  ): Try[api.ConsumerPipe[T]] = {
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
  def open: Try[api.ProducerPipe[T]] = {
    try {
      consumerPacketPipes = actors.collect({
        case x: (Int, C) => {
          x._2.open(getSelf) match {
            case Success(producerPipe: api.ProducerPipe[T]) =>
              (x._1, producerPipe)

            case Failure(t: Throwable) =>
              throw t
          }
        }
      })

      Success(
        new ProducerPipe[P, C, T](
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
