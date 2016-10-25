package com.resolvix.concurrent.api

import scala.util.Try

/**
  *
  * @tparam P
  *   refers to the type of the producer
  *
  * @tparam C
  *   refers to the type of the consumer
  *
  * @tparam T
  *   refers to the type of values to be transmitted by the producer
  *   to the consumer
  *
  */
trait Producer[P <: Producer[P, C, T, V], C <: Consumer[C, P, T, V], T, V]
  extends Actor[P, C, T, V]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    consumer: C
  ): Try[Boolean]

  override def open(
    consumer: C
  ): Try[T]

  override def register(
    consumer: C
  ): Try[Boolean]

  override def unregister(
    consumer: C
  ): Try[Boolean]
}
