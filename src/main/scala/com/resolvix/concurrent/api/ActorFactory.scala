package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ActorFactory[L <: Actor[L, R, T, V], R <: Actor[R, L, T, V], T, V] {

  def newInstance: Actor[L, R, T, V]

}
