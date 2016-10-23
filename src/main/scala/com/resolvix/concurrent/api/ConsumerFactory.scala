package com.resolvix.concurrent.api

/**
  * Created by rwbisson on 23/10/16.
  */
trait ConsumerFactory[CF <: ConsumerFactory[CF, C, P, T], C <: Consumer[C, P, T], P <: Producer[P, C, T], T]
{

  def newInstance: C

}
