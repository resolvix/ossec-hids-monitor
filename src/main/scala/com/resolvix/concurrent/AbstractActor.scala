package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration, Producer}

import scala.util.{Failure, Success, Try}

object AbstractActor
{
  sealed class Packet[T](
    private val actor: api.Actor[T],
    private val t: T
  ) {
    /**
      *
      * @return
      */
    protected def getActor[A <: api.Actor[T]]: A = {
      actor
    }

    /**
      *
      * @return
      */
    def getT: T = {
      t
    }
  }
}

/**
  * Created by rwbisson on 20/10/16.
  */
trait AbstractActor[T]
  extends api.Actor[T]
{
  override def close[A <: api.Actor[T](
    actor: A
  ): Try[T] = {

  }

  override def open[A <: api.Actor[T](
    actor: A
  ): Try[Pipe[T]] = {

  }

  /**
    *
    * @param actor
    * @tparam T
    */
  sealed class myPipe[T](
    actor: api.Actor[T]
  ) {

    val pipe: Pipe[AbstractActor.Packet[T]] = new Pipe[AbstractActor.Packet[T]]()

    /**
      *
      * @param t
      * @return
      */
    def write(
      t: T
    ): Try[Boolean] = {
      val pT: AbstractActor.Packet[T]
      = new AbstractActor.Packet[T](
        actor,
        t
      )
      pipe.write(pT)
    }
  }

  /**
    *
    */
  val pipe: Pipe[AbstractActor.Packet[T]] = new Pipe[AbstractActor.Packet[T]]()

  /**
    *
    */
  protected var actors: List[api.Actor[T]] = List[api.Actor[T]]()

  /**
    *
    * @return
    */
  protected def consume: Try[T] = {
    pipe.read match {
      case Success(pT: AbstractActor.Packet[T]) =>
        Success(pT.getT)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  protected def consume(
    timeout: Int,
    unit: TimeUnit
  ): Try[T] = {
    pipe.read(timeout, unit) match {
      case Success(pT: AbstractActor.Packet[T]) =>
        Success(pT.getT)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  def initialise(
    configuration: Configuration
  ): Try[Boolean]


  def register[A <: api.Actor[T](
    actor: A
  ): Try[Boolean] = {
    Success(false)
  }


  def unregister[A <: api.Actor[T](
    actor: A
  ): Try[Boolean] = {
    Success(false)
  }
}
