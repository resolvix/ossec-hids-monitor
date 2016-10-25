package com.resolvix.concurrent

/**
  *
  * @tparam C
  * @tparam P
  */
trait ConsumerProducer[
  CF <: api.ConsumerFactory[CF, CC, PC, _, C],
  PC <: api.Producer[PC, CC, _, C],
  CC <: api.Consumer[CC, PC, _, C],
  C,
  PF <: api.ProducerFactory[PF, PP, CP, _, P],
  PP <: api.Producer[PP, CP, _, P],
  CP <: api.Consumer[CP, PP, _, P],
  P
] {

  /**
    *
    */
  private val consumer: CC = getConsumerFactory.newInstance

  /**
    *
    */
  private val producer: PP = getProducerFactory.newInstance

  /**
    *
    * @return
    */
  def getConsumerFactory: CF

  /**
    *
    * @return
    */
  def getConsumer: CC = this.consumer

  /**
    *
    * @return
    */
  def getProducerFactory: PF

  /**
    *
    * @return
    */
  def getProducer: PP = this.producer
}
