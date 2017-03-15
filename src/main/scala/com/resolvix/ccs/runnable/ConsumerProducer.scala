package com.resolvix.ccs.runnable

import scala.util.Try

trait ConsumerProducer[CP <: api.ConsumerProducer[CP, C, P], C, P]
  extends api.ConsumerProducer[CP, C, P]
{

  /**
    *
    */
  private class ConsumerProducerCP
    extends com.resolvix.ccs.ConsumerProducer[ConsumerProducerCP, C, P]
  {

    class RunnableConsumer
      extends ConsumerC
        with Consumer[C]
    {
      override def doConsume(c: C): Try[Boolean] = {
        ConsumerProducer.this.doConsume(c)
      }
    }

    class RunnableProducer
      extends ProducerP
        with Producer[P]
    {
      override def doProduce(): Try[P] = {
        ConsumerProducer.this.doProduce()
      }
    }

    override def createConsumerC: RunnableConsumer = new RunnableConsumer

    override def createProducerP: RunnableProducer = new RunnableProducer

    override def getConsumer: RunnableConsumer = {
      super.getConsumer.asInstanceOf[RunnableConsumer]
    }

    override def getProducer: RunnableProducer = {
      super.getProducer.asInstanceOf[RunnableProducer]
    }
  }

  /**
    *
    */
  private val consumerProducer: ConsumerProducerCP = new ConsumerProducerCP

  def doConsume(c: C): Try[Boolean]

  def doProduce(): Try[P]

  override def getConsumer: Consumer[C] = {
    consumerProducer.getConsumer.asInstanceOf[Consumer[C]]
  }

  override def getProducer: Producer[P] = {
    consumerProducer.getProducer.asInstanceOf[Producer[P]]
  }

  override def register[CP2 <: com.resolvix.ccs.api.Consumer[P]](
    consumer: CP2
  ): Try[Boolean] = {
    consumerProducer.register(consumer)
  }

  override def registerPP[PC2 <: com.resolvix.ccs.api.Producer[C]](
    producer: PC2
  ): Try[Boolean] = {
    consumerProducer.registerPP(producer)
  }

  override def registerP[CP2 <: com.resolvix.ccs.api.ConsumerProducer[CP2, P, _]](
    consumerProducer: CP2
  ): Try[Boolean] = {
    this.consumerProducer.registerP(consumerProducer)
  }

  override def registerC[PC <: com.resolvix.ccs.api.ProducerConsumer[PC, C, _]](
    producerConsumer: PC
  ): Try[Boolean] = {
    consumerProducer.registerC(producerConsumer)
  }

  override def crossregister[PC <: com.resolvix.ccs.api.ProducerConsumer[PC, C, P]](
    producerConsumer: PC
  ): Try[Boolean] = {
    consumerProducer.crossregister(producerConsumer)
  }
}
