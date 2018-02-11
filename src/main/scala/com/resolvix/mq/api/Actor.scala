package com.resolvix.mq

package api {

  /**
    * An actor, in the context of a message queue, is an identifiable producer
    * or consumer of messages.
    *
    */
  protected[mq] trait Actor {
    def getId: Int

    def getSelf: Actor
  }
}
