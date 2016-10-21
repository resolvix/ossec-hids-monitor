package com.resolvix.concurrent.xapi

import com.resolvix.concurrent.api.{Configuration, Pipe}

import scala.util.Try

trait Consumer[T]
  extends Actor[T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close(
    consumer: Consumer[T]
  ): Try[Boolean]

  override def open(
    consumer: Consumer[T]
  ): Try[Pipe[T]]

  override def register[P <: Producer[T]](
    producer: P
  ): Try[Boolean]

  override def unregister[P <: Producer[T]](
    producer: P
  ): Try[Boolean]
}