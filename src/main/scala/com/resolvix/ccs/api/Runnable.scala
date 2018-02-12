package com.resolvix.ccs.api

trait Runnable
  extends java.lang.Runnable
{
  def finish(): Unit

  def finished(): Unit

  def isFinished: Boolean

  def isFinishing: Boolean

  def isRunning: Boolean

  def isStopped: Boolean

  def run(): Unit

  def start(): Unit
}
