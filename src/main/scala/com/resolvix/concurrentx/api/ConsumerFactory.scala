package com.resolvix.concurrentx.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ConsumerFactory[
  CF <: ConsumerFactory[CF, C, P, V],
  C <: Consumer[C, P, V],
  P <: Producer[P, C, V],
  V
] extends ActorFactory[CF, P, C, V]
{

  def newInstance: C

}
