package com.resolvix.ccs.api

import scala.util.Try

trait RunnableProducer[V]
  extends com.resolvix.ccs.api.Producer[V]
    with Runnable
{

  /**
    *
    * @return
    */
  def doProduce(): Try[V]

  /**
    *
    */
  def run(): Unit
}
