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
    * @tparam V
    * @return
    */
  def getReader[R <: Reader[V], V](
    consumer: Any
  ): R

  /**
    *
    * @param producer
    * @param consumer
    * @tparam W
    * @tparam R
    * @tparam V
    * @return
    */
  def getWriter[W <: Writer[V], R <: Reader[V], V](
    producer: Any,
    consumer: Any
  ): W
}
