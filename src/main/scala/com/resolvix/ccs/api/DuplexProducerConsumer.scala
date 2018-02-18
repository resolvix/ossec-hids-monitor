package com.resolvix.ccs.api

trait DuplexProducerConsumer[DPC <: DuplexProducerConsumer[DPC, LP, LC, RP, RC], LP, LC, RP, RC]
  extends DuplexConsumerProducer[DPC, LC, LP, RC, RP]

//trait DuplexProducerConsumer[DPC <: DuplexConsumerProducer[DPC, LC, LP, RC, RP], LP, LC, RP, RC]
//  extends DuplexConsumerProducer[DPC, LC, LP, RC, RP]
