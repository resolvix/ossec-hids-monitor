package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ActorFactory[L <: Actor[L, R, V], R <: Actor[R, L, V], V] {

  def newInstance: Actor[L, R, V]

}
