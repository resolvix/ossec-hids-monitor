package com.resolvix.concurrentx

/**
  * Created by rwbisson on 22/10/16.
  */
class Packet[
  S <: api.Actor[S, D, V],
  D <: api.Actor[D, S, V],
  V
](
  source: S,
  destination: D,
  v: V
) {

  /**
    *
    * @return
    */
  def getDestination: D = {
    destination
  }

  /**
    *
    * @return
    */
  def getSource: S = {
    source
  }

  /**
    *
    * @return
    */
  def getV: V = {
    v
  }
}
