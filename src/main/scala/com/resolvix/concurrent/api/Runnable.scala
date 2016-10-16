package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 16/10/16.
  */
trait Runnable
  extends java.lang.Runnable
{

  private var runtimeControl: Boolean = false

  def isRunning: Boolean = {
    this.synchronized { runtimeControl }
  }

  def isStopped: Boolean = {
    this.synchronized { !runtimeControl }
  }

  def start(): Unit = {
    this.synchronized { runtimeControl = true }
  }

  /**
    *
    */
  def run(): Unit
}
