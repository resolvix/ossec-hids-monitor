package com.resolvix.mq

/**
  * Created by rwbisson on 22/10/16.
  */
class Message[W, R, V](
  writer: W,
  reader: R,
  v: V
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
