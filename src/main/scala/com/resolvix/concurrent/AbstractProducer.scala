package com.resolvix.concurrent

import com.resolvix.concurrent.api.{Configuration, Consumer, Producer}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

object AbstractProducer {

}

/**
  * Created by rwbisson on 19/10/16.
  */
trait AbstractProducer[P <: Producer[T], T]
  extends AbstractActor[P, T]
    with Producer[T]
{
  /**
    *
    * @param producer
    * @return
    */
  override def close(
    producer: Producer[T]
  ): Try[Boolean] = {
    /*consumers.foreach(
  (consumer: AbstractProducer.Consumer[T]) =>
    consumer.getSink.close(this)
)*/
    super.close(producer)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def open(
    producer: Producer[T]
  ): Try[api.Pipe[T]] = {
    /*consumers.foreach(
  (consumer: AbstractProducer.Consumer[T]) =>
    consumer.getSink.open(this)
)*/
    super.open(producer)
  }

  /**
    *
    * @param t
    */
  def produce(
    t: T
  ): Try[Boolean] = {
    /*consumers.foreach(
      (c: AbstractProducer.Consumer[T]) => {
        try {
          c.getSink.write(t)
        } catch {
          case t: Throwable =>
          //
          //  Do nothing
          //
        }
      }
    )*/
    Success(true)
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
    * @param producer
    * @return
    */
  override def register[P <: Producer[T]](
    producer: P
  ): Try[Boolean] = {
    super.register(producer)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def unregister[P <: Producer[T]](
    producer: P
  ): Try[Boolean] = {
    super.unregister(producer)
  }
}
