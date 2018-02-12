package com.resolvix.ccs

package impl {

  trait ProducerConsumer[PC <: ProducerConsumer[PC, P, C], P, C]
    extends ConsumerProducer[PC, C, P]
      with api.ProducerConsumer[PC, P, C]

}