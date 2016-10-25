package com.resolvix.concurrent.api

import scala.util.Try

/**
  *
  * @tparam C
  *   refers to the type of the consumer
  *
  * @tparam P
  *   refers to the type of the producer
  *
  * @tparam T
  *   refers to the type of transport for the transmission of values of
  *   type V from the producer to the consumer
  *
  * @tparam V
  *   refers to the type of values to be received by the consumer
  *   from the producer
  *
  */
trait Consumer[C <: Consumer[C, P, T, V], P <: Producer[P, C, T, V], T, V]
  extends Actor[C, P, T, V]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    producer: P
  ): Try[Boolean]

  override def open(
    producer: P
  ): Try[T]

  override def register(
    producer: P
  ): Try[Boolean]

  override def unregister(
    producer: P
  ): Try[Boolean]
}