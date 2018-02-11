package com.resolvix.sio

import com.resolvix.sio.impl.StreamImpl

import scala.util.Try

object StreamFactory {
  def newStream[V](): api.Stream[V] = {
    new StreamImpl[V]()
  }
}
