package com.resolvix.concurrent

/**
  * Created by rwbisson on 22/10/16.
  */
class Packet[S <: api.Actor[S, D, _, V], D <: api.Actor[D, S, _, V], V](
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
