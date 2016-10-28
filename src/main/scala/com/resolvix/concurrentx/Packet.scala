package com.resolvix.concurrentx

/**
  * Created by rwbisson on 22/10/16.
  */
class Packet[
  S <: api.Actor[S, _ <: api.Transport[V], D, _ <: api.Transport[V], V],
  D <: api.Actor[D, _ <: api.Transport[V], S, _ <: api.Transport[V], V],
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
