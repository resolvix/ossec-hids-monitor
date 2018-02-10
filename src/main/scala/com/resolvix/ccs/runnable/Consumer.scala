package com.resolvix.ccs.runnable

import java.util.concurrent.TimeUnit

import com.resolvix.mq.api.Reader

import scala.concurrent.TimeoutException
import scala.util.{Failure, Success, Try}

trait Consumer[V]
  extends api.Consumer[V]
{
  /**
    *
    * @return
    */
  def doConsume(v: V): Try[Boolean]

  /**
    *
    */
  override def run(): Unit = {
    start()

    val reader: Reader[V] = open match {
      case Success(reader: Reader[V]) =>
        reader

      case Failure(t: Throwable) =>
        throw t
    }

    while (isRunning) {
      reader.read(5000, TimeUnit.MILLISECONDS) match {
        case Success(x: (Int, V @unchecked)) =>
          doConsume(x._2)

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
