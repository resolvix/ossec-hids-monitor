package com.resolvix.ccs

import com.resolvix.ccs.api._

import scala.util.control.NonFatal
import scala.util.{Success, Try}


trait ProducerConsumer[
  PC <: ProducerConsumer[PC, P, C],
  P,
  C
] extends api.ProducerConsumer[PC, P, C] {

  /**
    *
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
      Success(true)
    }
  }

  /**
    *
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
  private val consumer: ConsumerC = createConsumerC

  /**
    *
    */
  private val producer: ProducerP = createProducerP

  /**
    *
    * @return
    */
  protected def createConsumerC: ConsumerC = new ConsumerC

  /**
    *
    * @return
    */
  protected def createProducerP: ProducerP = new ProducerP

  /**
    *
    * @return
    */
  def getConsumer: Consumer[C] = this.consumer

  /**
    *
    * @return
    */
  def getProducer: Producer[P] = this.producer

  /**
    *
    * @param consumer
    * @tparam CP2
    * @return
    */
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
    * Register a ConsumerProducer to consume the produce generated by the
    * instant ProducerConsumer.
    *
    * @param consumerProducer
    * @tparam CP
    * @return
    */
  override def registerP[CP <: api.ConsumerProducer[CP, P, _]](
    consumerProducer: CP
  ): Try[Boolean] = {

    getProducer.register(
      consumerProducer.getConsumer
    )

    Success(true)
  }

  /**
    *
    * @param producerConsumer
    * @tparam PC2
    * @return
    */
  override def registerC[PC2 <: api.ProducerConsumer[PC2, C, _]](
    producerConsumer:PC2
  ): Try[Boolean] = {

    getConsumer.register(
      producerConsumer.getProducer
    )

    Success(true)
  }

  /**
    *
    * @param consumerProducer
    * @tparam CP
    * @return
    */
  override def crossRegister[CP <: api.ConsumerProducer[CP, P, C]](
    consumerProducer: CP
  ): Try[Boolean] = {
    val consumerP: api.Consumer[P] = consumerProducer.getConsumer

    try {
      getProducer.register(consumerP)
      consumerP.register(getProducer)
    } catch {
      case NonFatal(t: Throwable) => throw t
    }

    val producerC: api.Producer[C] = consumerProducer.getProducer

    try {
      getConsumer.register(producerC)
      producerC.register(getConsumer)
    } catch {
      case NonFatal(t: Throwable) => throw t
    }
  }
}