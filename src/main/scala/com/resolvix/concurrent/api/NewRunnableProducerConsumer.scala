package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 16/10/16.
  */
trait NewRunnableProducerConsumer[C, P]
  extends ConsumerProducer[C, P]
    with Runnable
{
  override def run(): Unit = {

  }
}
