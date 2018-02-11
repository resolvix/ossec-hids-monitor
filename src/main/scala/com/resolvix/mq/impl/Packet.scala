package com.resolvix.mq

package impl {
  /**
    *
    * @param writer
    *
    * @param reader
    *
    * @param v
    *
    * @tparam V
    */
  private class Packet[V](
    writer: Int,
    reader: Int,
    v: V
  ) extends Message[Int, Int, V](
    writer,
    reader,
    v
  )
}
