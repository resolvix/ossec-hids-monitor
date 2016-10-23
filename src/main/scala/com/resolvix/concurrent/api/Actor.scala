package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api

import scala.util.{Success, Try}

/**
  * Created by rwbisson on 16/10/16.
  */
trait Actor[L <: Actor[L, R, V], R <: Actor[R, L, V], V] {

  /**
    *
    * @param actor
    * @return
    */
  def close(
    actor: R
  ): Try[Boolean]

  /**
    *
    * @return
    */
  def getId: Int

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
  def isRegistered(
    actor: R
  ): Boolean

  /**
    * The open method specifying a remote actor provides the requesting
    * remote actor with a pipe suitable for the local / remote actor
    * relationship.
    *
    * In the context of a consumer, open(producer) generates a producer
    * pipe that enables the calling producer to write values to the pipe.
    *
    * In the context of a producer, open(consumer) reflects the calling
    * consumer to supply the pipe that enables the calling consumer to
    * read values from the pipe.
    *
    * @param actor
    * @return
    */
  def open(
    actor: R
  ): Try[Pipe[V]]

  /**
    * The open method without parameters provides the requestor with a
    * a pipe suitable for the local / remote actor relationship.
    *
    * @return
    */
  def open: Try[Pipe[V]]

  /**
    *
    * @param actor
    * @return
    */
  def register(
    actor: R
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @return
    */
  def unregister(
    actor: R
  ): Try[Boolean]
}
