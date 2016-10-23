package com.resolvix.concurrent

/**
  *
  * @tparam C
  * @tparam P
  */
trait ConsumerProducer[
  PC <: api.Producer[PC, CC, C],
  CC <: api.Consumer[CC, PC, C],
  C,
  PP <: api.Producer[PP, CP, P],
  CP <: api.Consumer[CP, PP, P],
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
  def getConsumerFactory[CF <: api.ConsumerFactory[CF, CC, PC, C]]: api.ConsumerFactory[CF, CC, PC, C]

  /**
    *
    * @return
    */
  def getConsumer: CC = this.consumer

  /**
    *
    * @return
    */
  def getProducerFactory[PF <: api.ProducerFactory[PF, PP, CP, P]]: api.ProducerFactory[PF, PP, CP, P]

  /**
    *
    * @return
    */
  def getProducer: PP = this.producer
}
