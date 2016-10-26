package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ProducerFactory[
  PF <: ProducerFactory[PF, P, C, V],
  P <: Producer[P, C, _ <: ProducerPipe[V], V],
  C <: Consumer[C, P, _ <: ConsumerPipe[V], V],
  V
] extends ActorFactory[PF, P, C, V]
{

  def newInstance: P

}
