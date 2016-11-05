package com.resolvix.sio.api

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 31/10/16.
  */
trait Reader[V]
{
  /**
    *
    * @return
    */
  def read: Try[V]

  /**
    *
    * @param timeout
    * @param unit
    * @return
    */
  def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[V]
}