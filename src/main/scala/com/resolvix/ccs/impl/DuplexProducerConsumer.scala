package com.resolvix.ccs

package impl {

  trait DuplexProducerConsumer[DPC <: DuplexProducerConsumer[DPC, LP, LC, RP, RC], LP, LC, RP, RC]
    extends DuplexConsumerProducer[DPC, LC, LP, RC, RP]

}