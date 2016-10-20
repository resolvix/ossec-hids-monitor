package com.resolvix.concurrent.api
import scala.util.Try

/**
  * Created by rwbisson on 20/10/16.
  */
trait Consumer[T]
  extends Actor[T]
{
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  override def close[C <: Consumer[T]](consumer: C): Try[T]

  override def open[C <: Consumer[T]](consumer: C): Try[Pipe[T]]

  override def register[C <: Consumer[T]](consumer: C): Try[Boolean] = ???

  override def unregister[C <: Consumer[T]](consumer: C): Try[Boolean] = ???
}
