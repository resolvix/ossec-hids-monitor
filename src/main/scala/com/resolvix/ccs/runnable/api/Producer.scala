package com.resolvix.ccs.runnable.api

import scala.util.Try

trait Producer[V]
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
