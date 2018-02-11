package com.resolvix.mq.impl

import com.resolvix.sio.impl.StreamImpl

/**
  *
  */
class MessageStream[V]
  extends StreamImpl[Packet[V]] {

  override def getReader: Reader = {
    new Reader()
  }

  override def getWriter: Writer = {
    new Writer()
  }
}
