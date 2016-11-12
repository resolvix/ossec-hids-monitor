package com.resolvix.ccs.runnable.api

trait Consumer[V]
  extends com.resolvix.ccs.api.Consumer[V]
  with Runnable
{
  /**
    *
    * @return
    */
  def doConsume(v: V): Unit

  /**
    *
    */
  def run(): Unit
}
