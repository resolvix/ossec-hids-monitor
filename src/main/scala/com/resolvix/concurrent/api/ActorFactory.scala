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
  AF <: ActorFactory[AF, L, LT, R, RT, V],
  L <: Actor[L, LT, R, RT, V],
  LT <: Transport[V],
  R <: Actor[R, RT, L, LT, V],
  RT <: Transport[V],
  V
] {
  /**
    *
    * @return
    */
  def newInstance: Actor[L, LT, R, RT, V]
}
