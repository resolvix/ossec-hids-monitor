package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration

import scala.util.{Failure, Success, Try}

/**
  *
  */
object Actor
{
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
  *
  * @tparam L
  *   specifies the class of the local actor
  *
  * @tparam R
  *   specifies the class of the remote actors
  *
  * @tparam V
  *   specifies the class of values to be passed between the local actor
  *   and the remote actors
  */
trait Actor[
  L <: api.Actor[L, R, V],
  R <: api.Actor[R, L, V],
  V
] extends api.Actor[
  L,
  R,
  V
] {
  //
  //
  //
  private val id: Int = Actor.generateId

  /**
    *
    */
  protected var actors: Map[Int, R] = Map[Int, R]()

  /*protected def findRemoteActor(
    actor: R
  ): Try[RemoteActor] = {
    actors.find(
      { case (id: Int, ra: RemoteActor) => ra.getActor.equals(actor) }
    ) match {
      case Some((id: Int, remoteActor: RemoteActor)) =>
        Success(remoteActor)

      case None =>
        Failure(new NoSuchElementException())
    }
  }*/

  override def getId: Int = {
    id
  }

  protected def getSelf: L

  override def isRegistered(
    actor: R
  ): Boolean = {
    actors.get(actor.getId) match {
      case Some(actor: R @unchecked) =>
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
    try {
      actors += ((actor.getId, actor))
      if (!actor.isRegistered(this.asInstanceOf[L])) {
        actor.register(this.asInstanceOf[L])
      }

      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  override def unregister(
    actor: R
  ): Try[Boolean] = {
    try {
      if (isRegistered(actor)) {
        actors -= (id)
        Success(true)
      } else {
        Success(false)
      }
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }
}
