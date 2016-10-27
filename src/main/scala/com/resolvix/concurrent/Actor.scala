package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration}

import scala.concurrent.duration.TimeUnit

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
  L <: api.Actor[L, LT, R, RT, V],
  LT <: api.Transport[V],
  R <: api.Actor[R, RT, L, LT, V],
  RT <: api.Transport[V],
  V
] extends api.Actor[
  L,
  LT,
  R,
  RT,
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

  /**
    *
    */
  protected val packetPipe: PacketPipe[L, LT, R, RT, V] = new PacketPipe[L, LT, R, RT, V]()

  override def close(
    actor: R
  ): Try[Boolean] = {
    Success(true)
  }

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

  override def open(
    actor: R
  ): Try[Pipe[V]]

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
