package com.resolvix.ccs.api

import scala.util.Try

/**
  * Created by rwbisson on 09/11/16.
  */
trait ConsumerProducer[CP <: ConsumerProducer[CP, C, P], C, P] {

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

  def register(
    consumer: Consumer[P]
  ): Try[Boolean]

  def register(
    producer: Producer[C]
  ): Try[Boolean]

  /**
    * @param consumerProducerPC
    * @tparam CPPC
    * @return
    */
  def crossregister[CPPC <: ConsumerProducer[CPPC, P, C]](
    consumerProducerPC: CPPC
  ): Try[Boolean]
}
