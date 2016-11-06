package com.resolvix.concurrentx.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ProducerFactory[
  PF <: ProducerFactory[PF, P, C, V],
  P <: Producer[P, C, V],
  C <: Consumer[C, P, V],
  V
] extends ActorFactory[PF, P, C, V]
{

  def newInstance: P

}
