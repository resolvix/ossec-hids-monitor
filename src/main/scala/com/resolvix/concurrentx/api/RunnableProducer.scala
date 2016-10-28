package com.resolvix.concurrentx.api

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/2016.
  */
/*trait RunnableProducer[P, C, T]
  extends Producer[P, C, T]
    with Runnable
{

  def doProduce(): Try[T]

  def run(): Unit = {
    super.start()
    while (isRunning) {
      doProduce() match {
        case Success(t) =>
          //produce(t)

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
}*/
