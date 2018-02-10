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
  CF <: api.ConsumerFactory[CF, CC, CCT, PC, PCT, C],
  PC <: api.Producer[PC, PCT, CC, CCT, C],
  PCT <: api.Transport[C],
  CC <: api.Consumer[CC, CCT, PC, PCT, C],
  CCT <: api.Transport[C],
  C,
  PF <: api.ProducerFactory[PF, PP, PPT, CP, CPT, P],
  PP <: api.Producer[PP, PPT, CP, CPT, P],
  PPT <: api.Transport[P],
  CP <: api.Consumer[CP, CPT, PP, PPT, P],
  CPT <: api.Transport[P],
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
