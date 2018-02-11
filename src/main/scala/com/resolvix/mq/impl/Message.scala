package com.resolvix.mq

package impl {
  /**
    * Created by rwbisson on 22/10/16.
    */
  private class Message[W, R, V](
    writer: W,
    reader: R,
    v     : V
  ) {

    /**
      *
      * @return
      */
    def getReader(): R = {
      reader
    }

    /**
      *
      * @return
      */
    def getWriter(): W = {
      writer
    }

    /**
      *
      * @return
      */
    def getV: V = {
      v
    }
  }
}
