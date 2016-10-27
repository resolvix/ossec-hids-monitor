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
  *   type V from the producer to the consumer for supply to the producer
  *   upon receipt of a call to the open method
  *
  * @tparam V
  *   refers to the type of values to be received by the consumer
  *   from the producer
  *
  */
trait Consumer[
  C <: Consumer[C, CT, P, PT, V],
  CT <: Transport[V],
  P <: Producer[P, PT, C, CT, V],
  PT <: Transport[V],
  V
] extends Actor[C, CT, P, PT, V] {
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    producer: P
  ): Try[Boolean]

  override def open(
    producer: P
  ): Try[ProducerPipe[V]]

  override def register(
    producer: P
  ): Try[Boolean]

  override def unregister(
    producer: P
  ): Try[Boolean]
}