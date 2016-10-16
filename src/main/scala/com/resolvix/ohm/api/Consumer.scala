package com.resolvix.ohm.api

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeUnit}

import scala.collection.mutable
import scala.util.Try

/**
  * Created by rwbisson on 16/10/16.
  */
trait Consumer[T]
{
  protected val pipe: Pipe[T]

  /**
    *
    * @return
    */
  def getPipe: Pipe[T] = pipe

  /**
    *
    * @param producer
    */
  def open(
    producer: Producer[T]
  ): Unit = { }

  /**
    *
    * @return
    */
  def consume: Try[T] = {
    pipe.read
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
    pipe.read(timeout, unit)
  }

  /**
    *
    * @param producer
    */
  def close(
    producer: Producer[T]
  ): Unit = { }
}
