package com.resolvix.concurrentx

import com.resolvix.mq.MessageQueue

import scala.util.{Success, Try}

/**
  *
  */
class MulticastWriter[
  P <: Producer[P, C, V],
  C,
  V
] (
  writerMap: Map[Int, MessageQueue[V]#Writer[P, C]]
) extends com.resolvix.mq.api.Writer[MulticastWriter[P, C, V], P, V] {
  /**
    *
    * @param v
    * @return
    */
  override def write(
    v: V
  ): Try[Boolean] = {
    for ((y: Int, w: MessageQueue[V]#Writer[P, C]) <- writerMap) {
      w.write(v)
    }
    Success(true)
  }

  override def getId: Int = {
    throw new IllegalAccessException()
  }

  def getSelf: MulticastWriter[P, C, V] = this
}
