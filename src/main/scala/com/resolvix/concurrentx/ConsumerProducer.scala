package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.{Configuration}

import scala.util.{Success, Try}

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
  CP <: ConsumerProducer[CP, C, P],
  C,
  P
] extends api.ConsumerProducer[CP, C, P] {

  /**
    * Concrete implementation of a class applying the Consumer trait class to
    * enable classes using the ConsumerProducer trait to consume messages of
    * type C produced by a producer based on the Producer trait for messages
    * of type C.
    */
  class ConsumerC
    extends Consumer[C]
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
    extends Producer[P]
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
  def getConsumer: Consumer[C] = this.consumerC

  /**
    *
    * @return
    */
  def getProducer: Producer[P] = this.producerP

  override def register[CP2 <: api.Consumer[P]](
    consumer: CP2
  ): Try[Boolean] = ???

  /**
    *
    * @param producer
    * @tparam PC2
    * @return
    */
  override def registerPP[PC2 <: api.Producer[C]](
    producer: PC2
  ): Try[Boolean] = ???

  /**
    *
    * @param producerConsumer
    * @tparam PC
    * @return
    */
  override def crossregister[PC <: api.ProducerConsumer[PC, C, P]](
    producerConsumer: PC
  ): Try[Boolean] = {

    val consumerP: api.Consumer[P] = producerConsumer.getConsumer

    try {

      getProducer.register(
        consumerP
      )

      consumerP.register(getProducer)

    } catch {
      case e: Exception => throw e
    }

    val producerC: api.Producer[C] = producerConsumer.getProducer

    try {

      getConsumer.register(
        producerConsumer.getProducer
      )

      producerC.register(getConsumer)

    } catch {
      case e: Exception => throw e
    }
  }

  /**
    *
    * @param consumerProducer
    * @tparam CP2
    * @return
    */
  override def registerP[CP2 <: api.ConsumerProducer[CP2, P, _]](
    consumerProducer: CP2
  ): Try[Boolean] = {

    getProducer.register(
      consumerProducer.getConsumer
    )

    Success(true)
  }

  /**
    *
    * @param producerConsumer
    * @tparam PC
    * @return
    */
  override def registerC[PC <: api.ProducerConsumer[PC, C, _]](
    producerConsumer: PC
  ): Try[Boolean] = {

    getConsumer.register(
      producerConsumer.getProducer
    )

    Success(true)
  }
}