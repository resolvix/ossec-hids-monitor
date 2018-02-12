package com.resolvix.ccs

import com.resolvix.mq.api.Writer

import scala.util.{Failure, Success}

package impl {

  /**
    * Created by rwbisson on 11/11/16.
    */
  trait RunnableProducer[V]
    extends Producer[V]
    with Runnable
    with api.RunnableProducer[V] {

    /**
      *
      */
    override def run(): Unit = {
      start()

      val writer: Writer[V] = open.get

      while (isRunning) {
        doProduce() match {
          case Success(t) =>
            writer.write(t)

          case Failure(e: Exception) =>
          //
          //  Do nothing
          //

          case Failure(t: Throwable) =>
          //
          //  Do nothing
          //
        }
      }
    }
  }
}
