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
  *   refers to the type of transport for the transmission of values of
  *   type V from the producer to the consumer for supply to the consumer
  *   upon receipt of a call to the open method
  *
  * @tparam V
  *   refers to the type of values to be transmitted by the producer
  *   to the consumer
  *
  */
trait Producer[
  P <: Producer[P, PT, C, CT, V],
  PT <: Transport[V],
  C <: Consumer[C, CT, P, PT, V],
  CT <: Transport[V],
  V
] extends Actor[P, PT, C, CT, V] {
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    consumer: C
  ): Try[Boolean]

  override def open(
    consumer: C
  ): Try[CT]

  override def register(
    consumer: C
  ): Try[Boolean]

  override def unregister(
    consumer: C
  ): Try[Boolean]
}
