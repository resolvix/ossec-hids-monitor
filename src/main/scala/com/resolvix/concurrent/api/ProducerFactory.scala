package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ProducerFactory[PF <: ProducerFactory[PF, P, C, T], P <: Producer[P, C, T], C <: Consumer[C, P, T], T]
  extends ActorFactory[P, C, T]
{

  def newInstance: P

}
