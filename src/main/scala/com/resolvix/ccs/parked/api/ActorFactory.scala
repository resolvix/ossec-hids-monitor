package com.resolvix.concurrentx.api

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
  L <: Actor[L, R, V],
  R <: Actor[R, L, V],
  V
] {
  /**
    *
    * @return
    */
  def newInstance: Actor[L, R, V]
}
