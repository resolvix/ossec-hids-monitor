package com.resolvix.mq.api

import com.resolvix.mq.api
import com.resolvix.sio

import scala.concurrent.duration._
import scala.util.Try

/**
  * Created by rwbisson on 05/11/16.
  */
trait MessageQueue[V] {

  /**
    *
    * @param consumer
    * @tparam R
    * @tparam C
    * @tparam V
    * @return
    */
  def getReader[R <: Reader[R, C, V], C, V](
    consumer: C
  ): R

  /**
    *
    * @param producer
    * @param consumer
    * @tparam W
    * @tparam R
    * @tparam P
    * @tparam C
    * @tparam V
    * @return
    */
  def getWriter[W <: Writer[W, P, V], R <: Reader[R, C, V], P, C, V](
    producer: P,
    consumer: C
  ): W
}
