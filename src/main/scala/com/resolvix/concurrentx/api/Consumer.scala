package com.resolvix.concurrentx.api

import scala.concurrent.duration._
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
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends Actor[C, P, V] {

  /**
    *
    * @param configuration
    * @return
    */
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  def close(
    producer: P
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  def open(
    producer: P
  ): Try[Pipe[V].Producer]

  /**
    *
    * @param producer
    * @return
    */
  override def register(
    producer: P
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  override def unregister(
    producer: P
  ): Try[Boolean]
}