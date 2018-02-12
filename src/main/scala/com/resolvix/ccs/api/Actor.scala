package com.resolvix.ccs

import scala.util.Try

package api {

  /**
    * Provides basic Actor-Actor relationship management including
    * registration, and deregistration.
    *
    * @tparam L
    * refers to the type of the local actor
    *
    * @tparam R
    * refers to the type of the remote actor
    */
  private[ccs] trait Actor[L <: Actor[L, R], R <: Actor[R, L]] {

    /**
      *
      * @return
      */
    def getId: Int

    /**
      *
      * @param configuration
      *
      * @return
      */
    def initialise(
      configuration: Configuration
    ): Try[Boolean]

    /**
      *
      * @param actor
      *
      * @return
      */
    def isRegistered(
      actor: R
    ): Boolean

    /**
      *
      * @param actor
      *
      * @return
      */
    def register(
      actor: R
    ): Try[Boolean]

    /**
      *
      * @param actor
      *
      * @return
      */
    def unregister(
      actor: R
    ): Try[Boolean]
  }
}