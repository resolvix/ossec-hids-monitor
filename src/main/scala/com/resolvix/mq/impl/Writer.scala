package com.resolvix.mq

import scala.util.{Failure, Success, Try}

package impl {

  /**
    *
    * @param producer
    *
    */
  private class Writer[V](
    private val messageQueue: MessageQueue[V],
    private val producer: Any,
    private val consumer: Any
  ) extends Actor
    with api.Writer[V] {
    //
    //
    //
    val reader: Reader[V]
    = getReader(consumer) match {
      case Success(r: Reader[V]) => r
      case Failure(t: Throwable) => throw t
    }

    //
    //
    //
    val messageWriter: MessageStream[V]#Writer
    = MessageQueue.getMessageStream[V](reader.getId).getWriter

    /**
      *
      * @param consumer
      *
      * @return
      */
    def getReader(
      consumer: Any
    ): Try[api.Reader[V]] = {
      Try(
        messageQueue.getReader(
          consumer
        )
      )
    }

    /**
      *
      * @return
      */
    override def getSelf: Writer[V] = this

    /**
      *
      * @param consumer
      *
      * @return
      */
    def hasConsumer(consumer: Any): Boolean = {
      this.consumer.equals(consumer)
    }

    /**
      *
      * @param producer
      *
      * @return
      */
    def hasProducer(producer: Any): Boolean = {
      this.producer.equals(producer)
    }

    /**
      *
      * @param v
      *
      * @return
      */
    def write(
      v: V
    ): Try[Boolean] = {
      Try({
        val p = new Packet[V](this.getId, reader.getId, v)
        messageWriter.write(p)
        true
      })
    }
  }
}
