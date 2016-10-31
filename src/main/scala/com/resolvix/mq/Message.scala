package com.resolvix.mq

/**
  * Created by rwbisson on 22/10/16.
  */
class Message[
  S <: api.Identifiable,
  D <: api.Identifiable,
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
