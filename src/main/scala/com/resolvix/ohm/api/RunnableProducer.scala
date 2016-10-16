package com.resolvix.ohm.api

/**
  * Created by rwbisson on 16/10/2016.
  */
trait RunnableProducer[T]
  extends Producer[T]
    with Runnable
{
  //
  //
  //
  private var runtimeControl: Boolean = false

  def isRunning: Boolean = {
    this.synchronized { runtimeControl }
  }

  def isStopped: Boolean = {
    this.synchronized { !runtimeControl }
  }

  def run(): Unit
}
