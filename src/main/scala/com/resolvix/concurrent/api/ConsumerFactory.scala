package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ConsumerFactory[CF <: ConsumerFactory[CF, C, P, T, V], C <: Consumer[C, P, T, V], P <: Producer[P, C, T, V], T, V]
  extends ActorFactory[P, C, T, V]
{

  def newInstance: C

}
