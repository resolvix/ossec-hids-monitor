package com.resolvix.ccs.api

import scala.util.Try

trait RunnableConsumer[V]
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
