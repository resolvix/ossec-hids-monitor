package com.resolvix.mq.api

import com.resolvix.mq.api

/**
  * Created by rwbisson on 05/11/16.
  */
trait MessageQueue[V] {

  /**
    *
    * @tparam C
    * @tparam P
    */
  trait Consumer[
    C <: api.Actor,
    P <: api.Actor
  ] extends api.Consumer[(P, V)]

  /**
    *
    * @tparam P
    * @tparam C
    */
  trait Producer[
    P <: api.Actor,
    C <: api.Actor
  ] extends api.Producer[V]

  /**
    *
    * @param consumer
    * @tparam P
    * @tparam C
    * @return
    */
  def getConsumer[
  P <: api.Actor,
  C <: api.Actor
  ] (
    consumer: C
  ): Consumer[C, P]

  /**
    *
    * @param producer
    * @param consumer
    * @tparam P
    * @tparam C
    * @return
    */
  def getProducer[
    P <: api.Actor,
    C <: api.Actor
  ] (
    producer: P,
    consumer: C
  ): Producer[P, C]

}
