package com.resolvix.mq.api

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 05/11/2016.
  */
trait Reader[R <: Reader[R, C, V], C, V]
  extends Actor[R]
{
  /**
    *
    * @return
    */
  def read[W <: Writer[W, _, V]]: Try[(W, V)]

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read[W <: Writer[W, _, V]](
    timeout: Int,
    unit: TimeUnit
  ): Try[(W, V)]
}
