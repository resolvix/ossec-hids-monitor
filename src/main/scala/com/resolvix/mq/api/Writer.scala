package com.resolvix.mq.api

import scala.util.Try

/**
  * Created by rwbisson on 05/11/2016.
  */
trait Writer[W <: Writer[W, V], V]
  extends Actor[W]
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
