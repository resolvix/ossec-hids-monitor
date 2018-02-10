package com.resolvix.ccs.runnable.api

import scala.util.Try

trait Consumer[V]
  extends com.resolvix.ccs.api.Consumer[V]
  with Runnable
{
  /**
    *
    * @return
    */
  def doConsume(v: V): Try[Boolean]

  /**
    *
    */
  def run(): Unit
}
