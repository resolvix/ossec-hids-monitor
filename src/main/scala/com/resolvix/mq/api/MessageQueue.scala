package com.resolvix.mq.api

import com.resolvix.mq.api
import com.resolvix.sio

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 05/11/16.
  */
trait MessageQueue[V] {

  def getReader[R <: Reader[R, C, V], C, V](
    consumer: C
  ): Reader[R, C, V]

  /**
    *
    * @param writer
    * @param reader
    * @tparam W
    * @return
    */
  def getWriter[W <: Writer[W, P, V], P, V](
    producer: P,
    reader: Reader[_, _, V]
  ): Writer[W, P, V]

}
