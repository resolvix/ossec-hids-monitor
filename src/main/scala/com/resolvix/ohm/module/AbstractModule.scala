package com.resolvix.ohm.module

import java.util.concurrent.TimeUnit

import com.resolvix.ohm.api.{Alert, Module, ModuleAlertStatus}

import scala.concurrent._
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 18/10/16.
  */
abstract class AbstractModule[C <: Alert]
  extends Module[C]
{
  def doConsume(c: C): Try[Boolean]

  /*def run(): Unit = {
    super.start()
    while (super.isRunning || super.isFinishing) {
      getConsumer.rea(5000, TimeUnit.MILLISECONDS) match {
        case Success(c: C) =>
          doConsume(c)

        case Failure(e: TimeoutException) =>
          //
          //  Do nothing
          //
          if (super.isFinishing) {
            finished()
          }

        case Failure(t: Throwable) =>
          throw t
      }
    }
  }*/
}
