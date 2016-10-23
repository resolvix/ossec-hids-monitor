package com.resolvix.concurrent.api

import scala.util.Try

trait Consumer[C <: Consumer[C, P, T], P <: Producer[P, C, T], T]
  extends Actor[C, P, T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    producer: P
  ): Try[Boolean]

  override def open(
    producer: P
  ): Try[Pipe[T]]

  override def register(
    producer: P
  ): Try[Boolean]

  override def unregister(
    producer: P
  ): Try[Boolean]
}