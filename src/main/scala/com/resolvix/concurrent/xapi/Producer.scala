package com.resolvix.concurrent.xapi

import scala.util.Try

trait Producer[T]
  extends Actor[Producer[T], T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    producer: Producer[T]
  ): Try[Boolean]

  override def open(
    producer: Producer[T]
  ): Try[Pipe[T]]

  override def register[C <: Consumer[T]](
    consumer: C
  ): Try[Boolean]

  override def unregister[C <: Consumer[T]](
    consumer: C
  ): Try[Boolean]
}
