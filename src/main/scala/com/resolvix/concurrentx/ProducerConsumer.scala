package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration

import scala.util.Try


trait ProducerConsumer[
  PC <: ProducerConsumer[PC, CC, PP, P, C],
  CC <: Consumer[CC, PC, C],
  PP <: Producer[PP, PC, P],
  C,
  P
] {
  class ProducerC
    extends Producer[ProducerC, CC, C]
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
  private val consumer: ConsumerP = new ConsumerP

  /**
    *
    */
  private val producer: ProducerC = new ProducerC

  /**
    *
    * @return
    */
  def getConsumer: Consumer[ConsumerP, PP, P] = this.consumer

  /**
    *
    * @return
    */
  def getProducer: Producer[ProducerC, CC, C] = this.producer
}
