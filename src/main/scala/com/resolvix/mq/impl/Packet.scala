package com.resolvix.mq.impl

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
class Packet[V](
  writer: Int,
  reader: Int,
  v: V
) extends Message[Int, Int, V](
  writer,
  reader,
  v
)
