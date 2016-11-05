package com.resolvix.sio.api

/**
  * Created by rwbisson on 20/10/16.
  */
trait Stream[V]
{
  /**
    *
    * @return
    */
  def getReader(): Reader[V]

  /**
    *
    * @return
    */
  def getWriter: Writer[V]
}
