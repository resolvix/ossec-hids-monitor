package com.resolvix.ccs.runnable

import com.resolvix.ccs.runnable.api.{Consumer, Producer}

import scala.util.Try

/**
  * Created by rwbisson on 11/11/16.
  */
trait ProducerConsumer[PC <: ProducerConsumer[PC, P, C], P, C]
  extends com.resolvix.ccs.runnable.api.ProducerConsumer[PC, P, C]
{

  private class ProducerConsumerPC
    extends com.resolvix.ccs.ProducerConsumer[ProducerConsumerPC, P, C] {

    class RunnableConsumer
      extends ConsumerC
        with Consumer[C]
    {
      /**
        *
        * @return
        */
      override def doConsume(c: C): Try[Boolean] = {
        ProducerConsumer.this.doConsume(c)
      }
    }

    class RunnableProducer
      extends ProducerP
        with Producer[P]
    {
      /**
        *
        * @return
        */
      override def doProduce(): Try[P] = {
        ProducerConsumer.this.doProduce()
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

  private val producerConsumer: ProducerConsumerPC = new ProducerConsumerPC

  def doConsume(c: C): Try[Boolean]

  def doProduce(): Try[P]

  override def getConsumer: Consumer[C] = {
    producerConsumer.getConsumer
  }

  override def getProducer: Producer[P] = {
    producerConsumer.getProducer
  }

  override def register[CP2 <: com.resolvix.ccs.api.Consumer[P]](
    consumer: CP2
  ): Try[Boolean] = {
    producerConsumer.register(consumer)
  }

  override def registerPP[PC2 <: com.resolvix.ccs.api.Producer[C]](
    producer: PC2
  ): Try[Boolean] = {
    producerConsumer.registerPP(producer)
  }

  override def registerP[CP <: com.resolvix.ccs.api.ConsumerProducer[CP, P, _]](
    consumerProducer: CP
  ): Try[Boolean] = {
    producerConsumer.registerP(consumerProducer)
  }

  override def registerC[PC2 <: com.resolvix.ccs.api.ProducerConsumer[PC2, C, _]](
    producerConsumer: PC2
  ): Try[Boolean] = {
    this.producerConsumer.registerC(producerConsumer)
  }

  override def crossregister[CP <: com.resolvix.ccs.api.ConsumerProducer[CP, P, C]](
    consumerProducer: CP
  ): Try[Boolean] = {
    producerConsumer.crossregister(consumerProducer)
  }

  /*
    /**
    *
    * @param i
    * @return
    */
  def apply(i: C): P

  /**
    *
    * @return
    */
  override def doConsume(i: C): Unit = {
    val o: P = apply(i)
    //produce(o)
  }
   */

}
