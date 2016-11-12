package com.resolvix.ccs.runnable.api


trait ConsumerProducer[CP <: ConsumerProducer[CP, C, P], C, P]
  extends com.resolvix.ccs.api.ConsumerProducer[CP, C, P]
    with Runnable
{

}
