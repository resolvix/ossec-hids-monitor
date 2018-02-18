package com.resolvix.ccs

import com.resolvix.ccs.api.Configuration

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

package impl {

  /**
    *
    */
  private[impl] object Actor {
    //
    //
    //
    private var id: Int = 0x00

    /**
      *
      * @return
      */
    private def generateId: Int = {
      this.synchronized {
        id += 1
        id
      }
    }
  }

  /**
    * Provides basic Actor-Actor relationship management including
    * registration, and deregistration.
    *
    * @tparam L
    *   refers to the type of the local actor
    *
    * @tparam R
    *   refers to the type of the remote actor
    */
  private[impl] trait Actor[L <: api.Actor[L, R], R <: api.Actor[R, L]]
    extends api.Actor[L, R] {
    //
    //
    //
    private val id: Int = Actor.generateId

    /**
      *
      */
    protected var actors: Map[Int, R] = Map[Int, R]()

    override def getId: Int = {
      id
    }

    protected def getSelf: L

    override def isRegistered(
      actor: R
    ): Boolean = {
      actors.get(actor.getId) match {
        case Some(actor: R@unchecked) =>
          true

        case None =>
          false
      }
    }

    override def initialise(
      configuration: Configuration
    ): Try[Boolean]

    override def register(
      actor: R
    ): Try[Boolean] = {
      Try({
        actors += ((actor.getId, actor))
        if (!actor.isRegistered(this.asInstanceOf[L]))
          actor.register(this.asInstanceOf[L])
        true
      })
    }

    override def unregister(
      actor: R
    ): Try[Boolean] = {
      Try({
        if (isRegistered(actor)) {
          actors -= (id)
          true
        } else false
      })
    }
  }
}
