package com.resolvix.concurrent.xapi

import com.resolvix.concurrent.api.{Configuration, Pipe}

import scala.util.Try

/**
  * Created by rwbisson on 16/10/16.
  */
trait Actor[T] {

  /**
    *
    * @param configuration
    * @return
    */
  def initialise(
    configuration: Configuration
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def close(
    actor: Actor[T]
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def open(
    actor: Actor[T]
  ): Try[Pipe[T]]

  /**
    *
    * @param actor
    * @return
    */
  def register[B <: Actor[T]](
    actor: B
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def unregister[B <: Actor[T]](
    actor: B
  ): Try[Boolean]
}
