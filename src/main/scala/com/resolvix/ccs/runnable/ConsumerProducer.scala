package com.resolvix.ccs.runnable

trait ConsumerProducer[CP <: ConsumerProducer[CP, C, P], C, P]
{

  /**
    *
    */
  private class ConsumerProducerCP
    extends com.resolvix.ccs.ConsumerProducer[ConsumerProducerCP, C, P]
  {

  }

  /**
    *
    */
  private val consumerProducer: ConsumerProducerCP = new ConsumerProducerCP

}
