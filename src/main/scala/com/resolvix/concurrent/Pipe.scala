package com.resolvix.concurrent

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeoutException}

import scala.concurrent.Promise
import scala.concurrent.duration.TimeUnit
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/2016.
  */
class Pipe[T]
  extends api.Pipe[T]
    with api.ProducerPipe[T]
    with api.ConsumerPipe[T]
{

  //
  //
  //
  private val queue: BlockingQueue[T]
    = new LinkedBlockingQueue[T]()

  /**
    *
    * @return
    */
  def read: Try[T] = {
    try {
      Success(queue.take)
    } catch {
      case e: InterruptedException =>
        Failure(e)
    }
  }

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[T] = {
    try {
      val t: T = queue.poll(timeout, unit)
      if (t != null) {
        Success(t)
      } else {
        Failure(new TimeoutException)
      }
    } catch {
      case NonFatal(e: InterruptedException) =>
        Failure(e)
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
    try {
      Success(queue.offer(t))
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }
}
