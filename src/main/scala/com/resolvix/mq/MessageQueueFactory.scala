package com.resolvix.mq

object MessageQueueFactory {
  def newMessageQueue[V](): api.MessageQueue[V] = {
    new impl.MessageQueue[V]()
  }
}
