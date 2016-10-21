package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api

import scala.util.{Success, Try}

/**
  * Created by rwbisson on 16/10/16.
  */
trait Actor[A <: Actor[A, B, T], B <: Actor[A, B, T], T] {

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
    actor: A
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def open(
    actor: A
  ): Try[Pipe[T]]

  /**
    *
    * @param actor
    * @return
    */
  def register(
    actor: B
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def unregister(
    actor: B
  ): Try[Boolean]
}
