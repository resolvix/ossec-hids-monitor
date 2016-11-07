package com.resolvix.mq.api

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 05/11/2016.
  */
trait Reader[R <: Reader[R, V], V]
  extends Actor[R]
{
  /**
    *
    * @return
    */
  def read[W <: Writer[W, V]]: Try[(Int, V)]

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read[W <: Writer[W, V]](
    timeout: Int,
    unit: TimeUnit
  ): Try[(Int, V)]
}
