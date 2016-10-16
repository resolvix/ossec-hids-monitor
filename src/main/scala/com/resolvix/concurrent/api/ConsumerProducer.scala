package com.resolvix.concurrent.api

import com.resolvix.concurrent.Pipe

/**
  * Created by rwbisson on 16/10/16.
  */
trait ConsumerProducer[C, P] {

  /**
    *
    */
  object Consumer extends Consumer[C]
  {
    override val pipe: Pipe[C] = getConsumerPipe
  }

  /**
    *
    */
  object Producer extends Producer[P] {

  }

  /**
    *
    * @return
    */
  protected def getConsumerPipe: Pipe[C] = new Pipe[C]

  /**
    *
    * @return
    */
  def getConsumer: Consumer[C] = this.Consumer

  /**
    *
    * @return
    */
  def getProducer: Producer[P] = this.Producer

}
