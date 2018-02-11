package com.resolvix.mq

package impl {
  private[mq] abstract class Actor
    extends api.Actor {
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
}
