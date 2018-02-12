package com.resolvix.ccs.api

trait ProducerConsumer[PC <: ProducerConsumer[PC, P, C], P, C]
  extends ConsumerProducer[PC, C, P] { }
