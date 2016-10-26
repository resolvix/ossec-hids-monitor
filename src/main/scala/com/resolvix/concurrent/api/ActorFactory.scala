package com.resolvix.concurrent.api

/**
  *
  * @tparam AF
  * @tparam L
  * @tparam R
  * @tparam T
  * @tparam V
  */
trait ActorFactory[
  AF <: ActorFactory[AF, L, R, V],
  L <: Actor[L, R, _ <: Transport[V], V],
  R <: Actor[R, L, _ <: Transport[V], V],
  V
] {
  /**
    *
    * @return
    */
  def newInstance: Actor[L, R, Transport[V], V]
}
