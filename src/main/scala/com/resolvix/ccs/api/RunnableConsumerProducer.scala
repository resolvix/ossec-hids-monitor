package com.resolvix.ccs.api

trait RunnableConsumerProducer[CP <: RunnableConsumerProducer[CP, C, P], C, P]
  extends com.resolvix.ccs.api.ConsumerProducer[CP, C, P] { }
