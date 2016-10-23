package com.resolvix.concurrent.api

import scala.util.Try

trait Producer[P <: Producer[P, C, T], C <: Consumer[C, P, T], T]
  extends Actor[P, C, T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    consumer: C
  ): Try[Boolean]

  override def open(
    consumer: C
  ): Try[Pipe[T]]

  override def register(
    consumer: C
  ): Try[Boolean]

  override def unregister(
    consumer: C
  ): Try[Boolean]
}
