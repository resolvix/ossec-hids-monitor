package com.resolvix.ohm.api

trait ConsumerProducerModule[I, O]
  extends RunnableConsumer[I]
    with Producer[O]
{
  /**
    *
    * @param i
    * @return
    */
  def convert(i: I): O

  /**
    *
    * @return
    */
  override def doConsume(i: I): Unit = {
    val o: O = convert(i)
    produce(o)
  }
}
