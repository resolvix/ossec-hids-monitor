package com.resolvix.ccs.runnable.api

/**
  * Created by rwbisson on 16/10/16.
  */
trait Runnable
  extends java.lang.Runnable
{

  private var status: Int = 0

  def finish(): Unit = {
    this.synchronized { status = 2 }
  }

  def finished(): Unit = {
    this.synchronized { status = 3 }
  }

  def isFinished: Boolean = {
    this.synchronized { status == 3 }
  }

  def isFinishing: Boolean = {
    this.synchronized { status == 2 }
  }

  def isRunning: Boolean = {
    this.synchronized { status == 1 }
  }

  def isStopped: Boolean = {
    this.synchronized { status == 0 }
  }

  /**
    *
    */
  def run(): Unit

  def start(): Unit = {
    this.synchronized { status = 1 }
  }
}
