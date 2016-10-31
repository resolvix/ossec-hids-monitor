package com.resolvix.mq.api

import scala.util.Try

/**
  * Created by rwbisson on 31/10/16.
  */
trait Producer[V]
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
