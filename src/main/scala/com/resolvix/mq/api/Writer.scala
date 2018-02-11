package com.resolvix.mq.api

import scala.util.Try

/**
  * Created by rwbisson on 05/11/2016.
  */
trait Writer[V]
  extends Actor
{
  override def getSelf: Writer[V]

  /**
    *
    * @param v
    * @return
    */
  def write(
    v: V
  ): Try[Boolean]
}
