package com.resolvix.concurrentx
import com.resolvix.concurrentx.api.Configuration

import scala.util.Try

/**
  *

  * @tparam CP
  * @tparam PP
  * @tparam CC
  * @tparam C
  * @tparam P
  */
trait ConsumerProducer[
  CP <: ConsumerProducer[CP, PP, CC, C, P],
  PP <: Producer[PP, CP, C],
  CC <: Consumer[CC, CP, P],
  C,
  P
] {

  class ConsumerC
    extends Consumer[ConsumerC, CC, C]
  {
    /**
      *
      * @return
      */
    override protected def getSelf: ConsumerC = this

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

  class ProducerP
    extends Producer[ProducerP, PP, P]
  {
    /**
      *
      * @return
      */
    override protected def getSelf: ProducerP = this

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
  private val consumer: ConsumerC = new ConsumerC

  /**
    *
    */
  private val producer: ProducerP = new ProducerP

  /**
    *
    * @return
    */
  def getConsumer: ConsumerC = this.consumer

  /**
    *
    * @return
    */
  def getProducer: ProducerP = this.producer
}