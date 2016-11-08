package com.resolvix.concurrentx

import com.resolvix.mq.MessageQueue
import com.resolvix.mq.api.Writer

import scala.util.{Success, Try}

/**
  *
  */
class MulticastWriter[V](
  writerMap: Map[Int, Writer[V]]
) extends com.resolvix.mq.api.Writer[V] {
  /**
    *
    * @param v
    * @return
    */
  override def write(
    v: V
  ): Try[Boolean] = {
    for ((y: Int, w: Writer[V]) <- writerMap) {
      w.write(v)
    }
    Success(true)
  }

  override def getId: Int = {
    throw new IllegalAccessException()
  }

  override def getSelf: MulticastWriter[V] = this
}
