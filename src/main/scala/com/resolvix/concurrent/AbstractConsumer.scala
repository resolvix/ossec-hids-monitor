package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration, Consumer, Producer}

import scala.util.{Failure, Success, Try}

object AbstractConsumer
{

}

trait AbstractConsumer[C <: Consumer[C, P, T], P <: Producer[P, C, T], T]
  extends AbstractActor[C, P, T]
    with Consumer[C, P, T]
{
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
  ): Try[api.Pipe[T]] = {
    super.open(consumer)
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
  override def register(
    producer: P
  ): Try[Boolean] = {
    super.register(producer)
  }

  /**
    *
    * @param producer
    * @return
    */
  override def unregister(
    producer: P
  ): Try[Boolean] = {
    super.unregister(producer)
  }
}
