package com.resolvix.concurrentx

import com.resolvix.mq.MessageQueue

import scala.util.{Success, Try}

/**
  *
  */
class MulticastWriter[V](
  writerMap: Map[Int, MessageQueue[V]#Writer]
) extends com.resolvix.mq.api.Writer[MulticastWriter[V], V] {
  /**
    *
    * @param v
    * @return
    */
  override def write(
    v: V
  ): Try[Boolean] = {
    for ((y: Int, w: MessageQueue[V]#Writer) <- writerMap) {
      w.write(v)
    }
    Success(true)
  }

  override def getId: Int = {
    throw new IllegalAccessException()
  }

  def getSelf: MulticastWriter[V] = this
}
