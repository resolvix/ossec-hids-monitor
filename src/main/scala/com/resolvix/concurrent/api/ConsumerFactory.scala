package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ConsumerFactory[
  CF <: ConsumerFactory[CF, C, CT, P, PT, V],
  C <: Consumer[C, CT, P, PT, V],
  CT <: Transport[V],
  P <: Producer[P, PT, C, CT, V],
  PT <: Transport[V],
  V
] extends ActorFactory[CF, P, PT, C, CT, V]
{

  def newInstance: C

}
