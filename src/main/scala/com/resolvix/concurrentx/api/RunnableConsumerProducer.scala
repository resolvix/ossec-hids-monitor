package com.resolvix.concurrentx.api



/*trait RunnableConsumerProducer[I, O]
  extends RunnableConsumer[I]
    with Producer[O]
{
  /**
    *
    * @param i
    * @return
    */
  def apply(i: I): O

  /**
    *
    * @return
    */
  override def doConsume(i: I): Unit = {
    val o: O = apply(i)
    produce(o)
  }
}*/
