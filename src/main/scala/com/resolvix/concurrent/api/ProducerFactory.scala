package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ProducerFactory[
  PF <: ProducerFactory[PF, P, PT, C, CT, V],
  P <: Producer[P, PT, C, CT, V],
  PT <: Transport[V],
  C <: Consumer[C, CT, P, PT, V],
  CT <: Transport[V],
  V
] extends ActorFactory[PF, P, PT, C, CT, V]
{

  def newInstance: P

}
