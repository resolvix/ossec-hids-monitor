package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api

import scala.util.{Success, Try}

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
    * @tparam A
    * @return
    */
  def close[A <: Actor[T]](
    actor: A
  ): Try[T]

  /**
    *
    * @param actor
    * @tparam A
    * @return
    */
  def open[A <: Actor[T]](
    actor: A
  ): Try[Pipe[T]]

  /**
    *
    * @param actor
    * @tparam A
    * @return
    */
  def register[A <: Actor[T]](
    actor: A
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @tparam A
    * @return
    */
  def unregister[A <: Actor[T]](
    actor: A
  ): Try[Boolean]
}
