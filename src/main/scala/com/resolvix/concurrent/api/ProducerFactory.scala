package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ProducerFactory[PF <: ProducerFactory[PF, P, C, T, V], P <: Producer[P, C, T, V], C <: Consumer[C, P, T, V], T, V]
  extends ActorFactory[P, C, T, V]
{

  def newInstance: P

}
