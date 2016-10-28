package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration

import scala.util.Try

/**
  * Created by rwbisson on 28/10/16.
  */
trait ProducerConsumer[
  PC <: ProducerConsumer[PC, CP, P, C],
  CC <: Consumer[, PC, C],
  PP <: Producer[PP, CP, P],
  C,
  P
] {
  class ProducerC[CP]
    extends Producer[ProducerC, CP, C]
  {
    /**
      *
      * @return
      */
    override protected def getSelf: ProducerC = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
    super.initialise(configuration)
  }
  }

  class ConsumerP
    extends Consumer[ConsumerP, PP, P]
  {
    /**
      *
      * @return
      */
    override protected def getSelf: ConsumerP = this

    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
      super.initialise(configuration)
    }
  }

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
