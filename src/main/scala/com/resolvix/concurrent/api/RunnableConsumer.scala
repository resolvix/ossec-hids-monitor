package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import scala.concurrent.TimeoutException
import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 16/10/2016.
  */
trait RunnableConsumer[T]
  extends Consumer[T]
  with Runnable {
  /**
    *
    * @return
    */
  def doConsume(t: T): Unit

  /**
    *
    */
  def run(): Unit = {
    start()
    while (isRunning) {
      consume(5000, TimeUnit.MILLISECONDS) match {
        case Success(t) =>
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
}
