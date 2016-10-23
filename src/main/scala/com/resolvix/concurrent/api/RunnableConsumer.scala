package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import scala.concurrent.TimeoutException
import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 16/10/2016.
  */
trait RunnableConsumer[C, P, T]
  extends Consumer[C, P, T]
  with Runnable {
  /**
    *
    * @return
    */
  def doConsume(t: T): Unit

  /**
    *
    */
  def run(): Unit
}
