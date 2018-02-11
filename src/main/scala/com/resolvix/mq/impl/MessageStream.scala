package com.resolvix.mq

import com.resolvix.sio.impl.StreamImpl

package impl {
  /**
    *
    */
  private class MessageStream[V]
    extends StreamImpl[Packet[V]] {

    override def getReader: Reader = {
      new Reader()
    }

    override def getWriter: Writer = {
      new Writer()
    }
  }
}
