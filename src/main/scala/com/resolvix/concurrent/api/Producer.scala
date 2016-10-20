package com.resolvix.concurrent.api

import scala.util.{Success, Try}

trait Producer[T]
  extends Actor[T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close[P <: Producer[T]](
    producer: P
  ): Try[T]

  override def open[P <: Actor[T]](
    producer: P
  ): Try[Pipe[T]]

  override def register[P <: Producer[T]](
    producer: P
  ): Try[Boolean]

  override def unregister[P <: Producer[T]](
    producer: P
  ): Try[Boolean]
}
