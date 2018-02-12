package com.resolvix.ccs

import scala.util.Try

package impl {

  trait RunnableConsumerProducer[CP <: api.RunnableConsumerProducer[CP, C, P], C, P]
    extends ConsumerProducer[CP, C, P]
    with api.RunnableConsumerProducer[CP, C, P] {

    class RunnableConsumerC
      extends ConsumerC
      with RunnableConsumer[C] {

      override def doConsume(v: C): Try[Boolean] = RunnableConsumerProducer.this.doConsume(v)

    }

    class RunnableProducerP
      extends ProducerP
      with RunnableProducer[P] {

      override def doProduce(): Try[P] = RunnableConsumerProducer.this.doProduce()

    }

    protected def newRunnableConsumerC: api.RunnableConsumer[C] = new RunnableConsumerC

    protected def newRunnableProducerP: api.RunnableProducer[P] = new RunnableProducerP

    private val runnableConsumerC: api.RunnableConsumer[C] = getConsumer

    private val runnableProducerP: api.RunnableProducer[P] = getProducer

    def doConsume(c: C): Try[Boolean]

    def doProduce(): Try[P]

    override def getConsumer: api.RunnableConsumer[C] = {
      if (runnableConsumerC != null)
        return runnableConsumerC
      newRunnableConsumerC
    }

    override def getProducer: api.RunnableProducer[P] = {
      if (runnableProducerP != null)
        return runnableProducerP
      newRunnableProducerP
    }
  }
}
