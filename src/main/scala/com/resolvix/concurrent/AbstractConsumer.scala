package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.api.{Configuration, Consumer, Pipe, Producer}

import scala.util.{Failure, Success, Try}

object AbstractConsumer
{

}

/**
  * Created by rwbisson on 19/10/16.
  */
trait AbstractConsumer[T]
  extends Consumer[T]
{
  override def close[C <: Consumer[T](
    consumer: C
  ): Try[T] = {

  }

  override def open[C <: Consumer[T]](
    consumer: C
  ): Try[Pipe[T]] = {

  }

  /**
    *
    * @param producer
    */
  sealed class Pipe(
    producer: Producer[T]
  ) {

    val pipe: Pipe[AbstractConsumer.Packet[T]] = new Pipe[AbstractConsumer.Packet[T]]()

    /**
      *
      * @param t
      * @return
      */
    def write(
      t: T
    ): Try[Boolean] = {
      val pT: AbstractConsumer.Packet[T]
        = new AbstractConsumer.Packet[T](
          producer,
          t
        )
      pipe.write(pT)
    }
  }

  /**
    *
    */
  val pipe: Pipe[AbstractConsumer.Packet[T]] = new Pipe[AbstractConsumer.Packet[T]]()

  /**
    *
    */
  protected var producers: List[Producer[T]] = List[Producer[T]]()

  /**
    *
    * @return
    */
  protected def consume: Try[T] = {
    pipe.read match {
      case Success(pT: AbstractConsumer.Packet[T]) =>
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
      case Success(pT: AbstractConsumer.Packet[T]) =>
        Success(pT.getT)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

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
    * @param producer
    * @return
    */
  def register(
    producer: Producer[T]
  ): Try[Boolean] = {
    Success(false)
  }

  /**
    *
    * @param producer
    * @return
    */
  def unregister(
    producer: Producer[T]
  ): Try[Boolean] = {
    Success(false)
  }
}
