package com.resolvix.mq.api

/**
  * An actor, in the context of a message queue, is an identifiable producer
  * or consumer of messages.
  *
  */
trait Actor[A <: Actor[A]]
{
  def getId: Int

  def getSelf: A
}
