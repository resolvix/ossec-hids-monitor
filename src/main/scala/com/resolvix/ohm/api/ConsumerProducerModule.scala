package com.resolvix.ohm.api

trait ConsumerProducerModule[I, O]
  extends Consumer[I]
    with Producer[O]
{
  var run: Boolean = false

  def isRunning: Boolean = {
    this.synchronized {
      run
    }
  }

  def isStopped: Unit = {
    this.synchronized {
      !run
    }
  }

  def convert(i: I): O

  def execute(): Unit = {
    while (isRunning) {
      val i: I = read
      val o: O = convert(i)
      write(o)
    }
  }
}
