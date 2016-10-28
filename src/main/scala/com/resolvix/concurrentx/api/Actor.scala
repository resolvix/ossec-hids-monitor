package com.resolvix.concurrentx.api

import scala.util.Try

/**
  *
  * @tparam L
  *   refers to the type of the local actor
  *
  * @tparam R
  *   refers to the type of the remote actor
  *
  * @tparam V
  *   refers to the type of values to be transmitted between the local and
  *   remote actors
  *
  */
trait Actor[
  L <: Actor[L, R, V],
  R <: Actor[R, L, V],
  V
] {

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
