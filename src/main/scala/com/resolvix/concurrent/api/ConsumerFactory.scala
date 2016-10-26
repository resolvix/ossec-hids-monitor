package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ConsumerFactory[
  CF <: ConsumerFactory[CF, C, P, V],
  C <: Consumer[C, P, _ <: ConsumerPipe[V], V],
  P <: Producer[P, C, _ <: ProducerPipe[V], V],
  V
] extends ActorFactory[CF, P, C, V]
{

  def newInstance: C

}
