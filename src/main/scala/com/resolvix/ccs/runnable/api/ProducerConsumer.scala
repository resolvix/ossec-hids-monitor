package com.resolvix.ccs.runnable.api

trait ProducerConsumer[PC <: ProducerConsumer[PC, P, C], P, C]
  extends com.resolvix.ccs.api.ProducerConsumer[PC, P, C]
{

  override /**
    *
    * @return
    */
  def getConsumer: Consumer[C]

  override /**
    *
    * @return
    */
  def getProducer: Producer[P]

}
