package com.resolvix.concurrent.api

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 20/10/16.
  */
trait Pipe[T] {
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

  /**
    *
    * @param t
    * @return
    */
  def write(
    t: T
  ): Try[Boolean]
}
