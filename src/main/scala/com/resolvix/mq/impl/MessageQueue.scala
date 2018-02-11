package com.resolvix.mq

import scala.collection._

package impl {

  private object MessageQueue {

    //
    //  The identifier last allocated
    //
    private var lastAllocatedId: Int = 0x00

    //
    //
    //
    private val idMap: mutable.Map[Int, api.Actor]
    = mutable.Map[Int, api.Actor]()

    //
    //
    //
    private val messageStreamMap: mutable.Map[Int, MessageStream[_]]
    = mutable.Map[Int, MessageStream[_]]()

    /**
      *
      * @param actor
      *
      * @return
      */
    def allocateId(
      actor: api.Actor
    ): Int = {
      this.synchronized {
        lastAllocatedId += 1
        idMap.put(lastAllocatedId, actor)
        lastAllocatedId
      }
    }

    /**
      *
      * @param id
      *
      * @return
      */
    def getMessageStream[V](
      id: Int
    ): MessageStream[V] = {
      messageStreamMap.get(id) match {
        case Some(messageStream: MessageStream[_]) =>
          messageStream.asInstanceOf[MessageStream[V]]

        case None =>
          val messageStream: MessageStream[V]
          = new MessageStream[V]()
          messageStreamMap.put(id, messageStream)
          messageStream
      }
    }
  }

  /**
    * MessageQueue implements a multipoint-to-multipoint message queue intended
    * to support situations where one or more producers can send messages to
    * o zero, one or more consumers.
    *
    * @tparam V
    * refers to the type of messages intended to be written to and read
    * from the relevant message queue.
    */
  private[mq] class MessageQueue[V]
    extends api.MessageQueue[V] {
    /**
      *
      * @return
      */
    def getReader(
      consumer: Any
    ): api.Reader[V] = {
      MessageQueue.idMap.find({
        case (i: Int, r: Reader[V]) => r.hasConsumer(consumer)
        case _ => false
      }) match {
        case Some((i: Int, a: api.Actor)) =>
          a.asInstanceOf[Reader[V]]

        case _ =>
          val newReader: Reader[V] = new Reader[V](this, consumer)
          MessageQueue.idMap.put(newReader.getId, newReader)
          newReader
      }
    }

    /**
      *
      * @return
      */
    def getWriter(
      producer: Any,
      consumer: Any
    ): api.Writer[V] = {
      MessageQueue.idMap.find({
        case (i: Int, w: Writer[V]) =>
          w.hasProducer(producer) && w.hasConsumer(consumer)

        case _ => false
      }) match {
        case Some((i: Int, a: api.Actor)) =>
          a.asInstanceOf[Writer[V]]

        case _ =>
          val newWriter: Writer[V] = new Writer(this, producer, consumer)
          MessageQueue.idMap.put(newWriter.getId, newWriter)
          newWriter
      }
    }
  }
}
