package com.resolvix.concurrent

/**
  * Created by rwbisson on 22/10/16.
  */
class Packet[
  S <: api.Actor[S, ST, D, DT, V],
  ST <: api.Transport[V],
  D <: api.Actor[D, DT, S, ST, V],
  DT <: api.Transport[V],
  V
](
  actor: S,
  v: V
) {
  /**
    *
    * @return
    */
  protected def getActor: S = {
    actor
  }

  /**
    *
    * @return
    */
  def getV: V = {
    v
  }
}
