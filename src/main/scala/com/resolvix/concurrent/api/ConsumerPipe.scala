package com.resolvix.concurrent.api

import scala.concurrent.duration._
import scala.util.Try

trait ConsumerPipe[T]
  extends Pipe[T]
{

  /**
    *
    * @return
    */
  def read: Try[T]

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[T]
}
