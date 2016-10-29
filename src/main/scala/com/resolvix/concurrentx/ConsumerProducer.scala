package com.resolvix.concurrentx
import com.resolvix.concurrentx.api.Configuration

import scala.util.Try

/**
  * The ConsumerProducer trait provides a generic implementation of
  * functionality that supports consumption of messages of type C
  * coupled with production of messages of type P that enables either
  * of the following use cases -
  *
  * <ol>
  * <li>where the relevant implementation of the ConsumerProducer trait
  *     is to form a stage in a multistage pipeline involving the
  *     translation of incoming packets of type C to outgoing packets
  *     of type P; and</li>
  * <li>where the relevant implementation of the ConsumerProducer trait
  *     is to form an endpoint in which packets of type C are processed
  *     to form results of type P.</li>
  * </ol>
  *
  * @tparam COPR
  *   refers to the instance ConsumerProducer of messages of type C and P
  *   respectively
  *
  * @tparam PC
  *   refers to a Producer produing messages of type C
  *
  * @tparam CP
  *   refers to a Consumer accepting messages of type P
  *
  * @tparam C
  *   refers to the type of messages represented by C
  *
  * @tparam P
  *   refers to the type of messages represented by P
  */
trait ConsumerProducer[
  COPR <: ConsumerProducer[COPR, PC, CP, C, P],
  PC <: Producer[PC, COPR, C],
  CP <: Consumer[CP, COPR, P],
  C,
  P
] {

  /**
    * Concrete implementation of a class applying the Consumer trait class to
    * enable classes using the ConsumerProducer trait to consume messages of
    * type C produced by a producer based on the Producer trait for messages
    * of type C.
    */
  class ConsumerC
    extends Consumer[ConsumerC, PC, C]
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

  /**
    * Conrete implementation of a class applying the Producer abstract trait to
    * enable classes using the ConsumerProducer trait to produce messages of
    * type P consumed by a consumer based on the Consumer trait for messages of
    * type P.
    */
  class ProducerP
    extends Producer[ProducerP, CP, P]
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
  private val consumerC: ConsumerC = new ConsumerC

  /**
    *
    */
  private val producerP: ProducerP = new ProducerP

  /**
    *
    * @return
    */
  def getConsumer: ConsumerC = this.consumerC

  /**
    *
    * @return
    */
  def getProducer: ProducerP = this.producerP
}