package com.resolvix.ccs.impl

import scala.util.Try

trait RunnableConsumerProducer[CP <: com.resolvix.ccs.api.RunnableConsumerProducer[CP, C, P], C, P]
  extends com.resolvix.ccs.api.RunnableConsumerProducer[CP, C, P]
{

  /**
    *
    */
  private class ConsumerProducerCP
    extends ConsumerProducer[ConsumerProducerCP, C, P]
  {

    private class RunnableConsumer
      extends ConsumerC
        with com.resolvix.ccs.impl.RunnableConsumer[C]
    {
      override def doConsume(c: C): Try[Boolean] = {
        RunnableConsumerProducer.this.doConsume(c)
      }
    }

    class RunnableProducer
      extends ProducerP
        with com.resolvix.ccs.impl.RunnableProducer[P]
    {
      override def doProduce(): Try[P] = {
        RunnableConsumerProducer.this.doProduce()
      }
    }

    override def createConsumerC: ConsumerC = new RunnableConsumer

    override def createProducerP: ProducerP = new RunnableProducer

    override def getConsumer: com.resolvix.ccs.api.Consumer[C] = {
      super.getConsumer
    }

    override def getProducer: com.resolvix.ccs.api.Producer[P] = {
      super.getProducer
    }
  }

  /**
    *
    */
  private val consumerProducer: ConsumerProducerCP = new ConsumerProducerCP

  def doConsume(c: C): Try[Boolean]

  def doProduce(): Try[P]

  override def getConsumer: RunnableConsumer[C] = {
    consumerProducer.getConsumer.asInstanceOf[RunnableConsumer[C]]
  }

  override def getProducer: RunnableProducer[P] = {
    consumerProducer.getProducer.asInstanceOf[RunnableProducer[P]]
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
