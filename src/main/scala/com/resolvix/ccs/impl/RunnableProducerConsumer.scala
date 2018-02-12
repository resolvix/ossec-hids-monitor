package com.resolvix.ccs

package impl {
  trait RunnableProducerConsumer[PC <: RunnableProducerConsumer[PC, P, C], P, C]
    extends RunnableConsumerProducer[PC, C, P]
      with api.RunnableProducerConsumer[PC, P, C]
}
