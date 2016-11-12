package com.resolvix.ccs.runnable

import scala.util.Try

/**
  * Created by rwbisson on 11/11/16.
  */
trait ProducerConsumer[PC <: ProducerConsumer[PC, P, C], P, C]
{

  private class ProducerConsumerPC
    extends com.resolvix.ccs.ProducerConsumer[ProducerConsumerPC, P, C]
  {

    class RunnableConsumer
      extends ConsumerC
        with Consumer[C]
    {
      /**
        *
        * @return
        */
      override def doConsume(v: C): Unit = ???
    }

    class RunnableProducer
      extends ProducerP
        with Producer[P]
    {
      /**
        *
        * @return
        */
      override def doProduce(): Try[P] = ???
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
