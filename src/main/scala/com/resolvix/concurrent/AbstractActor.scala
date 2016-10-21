package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration, Producer}

import scala.concurrent.duration.TimeUnit

import scala.util.{Failure, Success, Try}

/**
  *
  */
object AbstractActor
{

  /**
    *
    * @param actor
    * @param t
    * @tparam A
    * @tparam T
    */
  sealed class Packet[A <: api.Actor[A, B, T], B <: api.Actor[B, A, T], T](
    private val actor: A,
    private val t: T
  ) {
    /**
      *
      * @return
      */
    protected def getActor: A = {
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

  /**
    *
    * @param actor
    * @tparam X
    * @tparam T
    */
  sealed class LocalActor[X <: api.Actor[X, Y, T], Y <: api.Actor[Y, X, T], T](
    actor: X
  ) {

    //
    //
    //
    private val id: Int = generateId

    /**
      *
      * @return
      */
    def getActor: X = {
      actor
    }

    /**
      *
      * @return
      */
    def getId: Int = {
      id
    }
  }

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
  * @tparam A
  * @tparam T
  */
trait AbstractActor[A <: api.Actor[A, B, T], B <: api.Actor[B, A, T], T]
  extends api.Actor[A, B, T]
{
  /**
    *
    * @param actor
    */
  sealed class LocalPipe(
    private val actor: A
  ) extends api.Pipe[T] {
    /**
      *
      * @return
      */
    override def read: Try[T] = {
      AbstractActor.this.pipe.read match {
        case Success(pT: AbstractActor.Packet[A, B, T]) =>
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
    override def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[T] = {
      AbstractActor.this.pipe.read(
        timeout,
        unit
      ) match {
        case Success(pT: AbstractActor.Packet[A, B, T]) =>
          Success(pT.getT)

        case Failure(t: Throwable) =>
          Failure(t)
      }
    }

    /**
      *
      * @param t
      * @return
      */
    def write(
      t: T
    ): Try[Boolean] = {
      val pT: AbstractActor.Packet[A, B, T]
      = new AbstractActor.Packet[A, B, T](
        actor,
        t
      )
      AbstractActor.this.pipe.write(pT)
    }
  }

  /**
    *
    */
  protected var actors: Map[Int, AbstractActor.LocalActor[B, A, T]]
    = Map[Int, AbstractActor.LocalActor[B, A, T]]()

  /**
    *
    */
  protected val pipe: Pipe[AbstractActor.Packet[A, B, T]]
    = new Pipe[AbstractActor.Packet[A, B, T]]()

  /**
    *
    * @param actor
    * @return
    */
  override def close(
    actor: A
  ): Try[Boolean] = {
    Success(true)
  }

  /**
    *
    * @param actor
    * @return
    */
  override def open(
    actor: A
  ): Try[api.Pipe[T]] = {
    try {
      Success(
        new LocalPipe(actor)
      )
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @return
    */
  def consume: Try[T] = {
    pipe.read match {
      case Success(pT: AbstractActor.Packet[A, B, T]) =>
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
  def consume(
    timeout: Int,
    unit: TimeUnit
  ): Try[T] = {
    pipe.read(timeout, unit) match {
      case Success(pT: AbstractActor.Packet[A, B, T]) =>
        Success(pT.getT)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  /**
    *
    * @param actor
    * @tparam B
    * @return
    */
  override def register(
    actor: B
  ): Try[Boolean] = {
    try {
      val la: AbstractActor.LocalActor[B, A, T]
        = new AbstractActor.LocalActor[B, A, T](actor)
      actors += ((la.getId, la))
      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  /**
    *
    * @param actor
    * @tparam B
    * @return
    */
  override def unregister(
    actor: B
  ): Try[Boolean] = {
    try {
      actors.find(
        { case (id: Int, la: AbstractActor.LocalActor[B, A, T]) => la.getActor.equals(actor) }
      ) match {
        case Some((id: Int, la: AbstractActor.LocalActor[B, A, T])) =>
          actors -= (id)
          Success(true)

        case None =>
          Success(false)
      }
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }
}
