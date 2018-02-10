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
    * @return
    */
  def getReader(
    consumer: Any
  ): Reader[V]

  /**
    *
    * @param producer
    * @param consumer
    * @return
    */
  def getWriter(
    producer: Any,
    consumer: Any
  ): Writer[V]
}
