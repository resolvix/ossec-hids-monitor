package com.resolvix.mq.api

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 05/11/2016.
  */
trait Reader[V]
  extends Actor
{
  /**
    *
    * @return
    */
  def read: Try[(Int, V)]

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[(Int, V)]
}
