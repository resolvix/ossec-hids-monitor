package com.resolvix.mq.impl

import com.resolvix.mq.api

abstract class Actor
  extends api.Actor
{
  //
  //  Allocate an identifier for the instant Reader.
  //
  val id: Int = MessageQueue.allocateId(getSelf)

  /**
    *
    * @return
    */
  def getId = id

  /**
    *
    * @return
    */
  def getSelf: Actor
}
