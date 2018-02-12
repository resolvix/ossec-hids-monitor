package com.resolvix.ccs

import java.util.concurrent.TimeUnit

import com.resolvix.mq.api.Reader

import scala.concurrent.TimeoutException
import scala.util.{Failure, Success, Try}

package impl {

  trait RunnableConsumer[V]
    extends Consumer[V]
    with Runnable
    with api.RunnableConsumer[V] {
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
          case Success((i: Int, v: V@unchecked)) =>
            doConsume(v)

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
}
