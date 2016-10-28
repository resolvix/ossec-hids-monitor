package com.resolvix.concurrentx
import com.resolvix.concurrentx.api.Configuration

import scala.util.Try

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
  CP <: ConsumerProducer[CP, PC, C, P],
  PC <: ProducerConsumer[PC, CP, P, C],
  C,
  P
] {

  class ProducerC
    extends Producer[]
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