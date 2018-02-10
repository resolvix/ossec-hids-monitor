package com.resolvix.ccs.runnable

import com.resolvix.mq.api.Writer

import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 11/11/16.
  */
trait Producer[V]
  extends api.Producer[V]
{

  /**
    *
    */
  override def run(): Unit = {
    super.start()

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
