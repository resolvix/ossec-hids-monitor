package com.resolvix.concurrent

/**
  *
  * @tparam CF
  * @tparam PC
  * @tparam CC
  * @tparam C
  * @tparam PF
  * @tparam PP
  * @tparam CP
  * @tparam P
  */
trait ConsumerProducer[
  CF <: api.ConsumerFactory[CF, CC, PC, C],
  PC <: api.Producer[PC, CC, _ <: api.Transport[C], C],
  CC <: api.Consumer[CC, PC, _ <: api.Transport[C], C],
  C,
  PF <: api.ProducerFactory[PF, PP, CP, P],
  PP <: api.Producer[PP, CP, _ <: api.Transport[P], P],
  CP <: api.Consumer[CP, PP, _ <: api.Transport[P], P],
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
