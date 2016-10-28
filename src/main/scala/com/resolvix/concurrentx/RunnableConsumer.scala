package com.resolvix.concurrentx

import java.util.concurrent.TimeUnit

import scala.concurrent.TimeoutException
import scala.util.{Failure, Success, Try}


/*trait RunnableConsumer[C <: Consumer[C, P, T], P <: Producer[P, C, T], T]
  extends api.Runnable
{
  /**
    *
    * @return
    */
  def doConsume(t: T): Unit

  /**
    *
    * @return
    */
  def open: Try[api.ConsumerPipe[T]]

  /**
    *
    */
  override def run(): Unit = {
    start()

    val consumerPipe: api.ConsumerPipe[T] = open match {
      case Success(consumerPipe: api.ConsumerPipe[T]) =>
        consumerPipe

      case Failure(t: Throwable) =>
        throw t
    }

    while (isRunning) {
      consumerPipe.read(5000, TimeUnit.MILLISECONDS) match {
        case Success(t: T @unchecked) =>
          doConsume(t)

        case Failure(e: TimeoutException) =>
          //
          //  Do nothing
          //

        case Failure(t: Throwable) =>
          throw t
      }
    }
  }
}*/
