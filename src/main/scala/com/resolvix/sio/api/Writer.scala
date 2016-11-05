package com.resolvix.sio.api

import scala.util.Try

/**
  * Created by rwbisson on 31/10/16.
  */
trait Writer[V]
{
  /**
    *
    * @param v
    * @return
    */
  def write(
    v: V
  ): Try[Boolean]
}
