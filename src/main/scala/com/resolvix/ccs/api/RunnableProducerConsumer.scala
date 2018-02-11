package com.resolvix.ccs.api

trait RunnableProducerConsumer[PC <: RunnableProducerConsumer[PC, P, C], P, C]
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
