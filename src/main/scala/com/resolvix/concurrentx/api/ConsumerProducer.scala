package com.resolvix.concurrentx.api

import scala.util.Try

/**
  * Created by rwbisson on 09/11/16.
  */
trait ConsumerProducer[
  CP <: ConsumerProducer[CP, C, P],
  C,
  P
] {

  /**
    *
    * @return
    */
  def getConsumer: Consumer[C]

  /**
    *
    * @return
    */
  def getProducer: Producer[P]

  /**
    *
    * @param consumer
    * @tparam CP2
    * @return
    */
  def register[CP2 <: Consumer[P]](
    consumer: CP2
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @tparam PC2
    * @return
    */
  def registerPP[PC2 <: Producer[C]](
    producer: PC2
  ): Try[Boolean]

  /**
    *
    * @param consumerProducer
    * @tparam CP2
    * @return
    */
  def registerP[CP2 <: ConsumerProducer[CP2, P, _]](
    consumerProducer: CP2
  ): Try[Boolean]

  /**
    * Register a ProducerConsumer to enable the consumption of ProducerConsumer
    * generated produce by the instant ConsumerProducer.
    *
    * @param producerConsumer
    * @tparam PC
    * @return
    */
  def registerC[PC <: ProducerConsumer[PC, C, _]](
    producerConsumer: PC
  ): Try[Boolean]

  /**
    *
    * @param producerConsumer
    * @tparam PC
    * @return
    */
  def crossregister[PC <: ProducerConsumer[PC, C, P]](
    producerConsumer: PC
  ): Try[Boolean]
}
