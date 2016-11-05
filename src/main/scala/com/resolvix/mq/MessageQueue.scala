package com.resolvix.mq

import com.resolvix.sio.StreamImpl

import scala.collection._
import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * The PacketPipe implements a multipoint-to-multippoint message queue
  * intended to support situations where multiple producers are able to
  * send messages to a multiple consumers.
  *
  *   refers to the type of the Producer intended to produce messages of
  *   type V
  *
  *   refers to the type of the Consumer intended to consume messages of
  *   type V
  *
  * @tparam V
  *   refers to the type of messages intended to be produced and consumed
  *   through this instant packet pipe.
  */
class MessageQueue[V]
  extends api.MessageQueue[V]
{

  abstract class Actor[A <: Actor[A]]
    extends api.Actor[A]
  {
    //
    //  Allocate an identifier for the instant Reader.
    //
    val id: Int = allocateId(getSelf)

    /**
      *
      * @return
      */
    def getId = id

    /**
      *
      * @return
      */
    def getSelf: A
  }

  /**
    * Objects of type Consumer[C] are intended to provide an interface
    * between the message stream for messages of type V that enables the
    * consumer to identify the producer of the message.
    *
    * @tparam R
    *   specifies the type of the identifier for Consumer objects intended
    *   to consume messages of type V
    *
    * @tparam W
    */
  class Reader[C](
    consumer: C
  ) extends Actor[Reader[C]]
      with api.Reader[Reader[C], C, V] {

    //
    //  Obtain a Reader object for the underlying message stream.
    //
    val messageConsumer: MessageStream#Reader
      = getMessageStream(getId).getReader

    override def getSelf: Reader[C] = this

    /**
      *
      * @param producer
      * @tparam P
      * @return
      */
    def getWriter[P](
      producer: P
    ): Try[Writer[P]] = {
      Success(
        new Writer[P](producer, getSelf)
      )
    }

    /**
      *
      * @return
      */
    def read[W <: api.Writer[W, _, V]]: Try[(W, V)] = {
      messageConsumer.read match {
        case Success(p: Packet[V]) =>
          val x: Writer[_] = p.getWriter()
          Success(
            (x.asInstanceOf[W], p.getV)
          )

        case Failure(t: Throwable) =>
          Failure(t)
      }
    }

    /**
      *
      * @param timeout
      * @param unit
      * @return
      */
    def read[W <: api.Writer[W, _, V]](
      timeout: Int,
      unit: TimeUnit
    ): Try[(Writer[_], V)] = {
      messageConsumer.read(timeout, unit) match {
        case Success(p: Packet[V]) =>
          Success(
            (p.getWriter(), p.getV)
          )

        case Failure(t: Throwable) =>
          Failure(t)
      }
    }
  }

  /**
    *
    * @param producer
    *
    * @param reader
    */
  class Writer[P](
    producer: P,
    reader: Reader[_]
  ) extends Actor[Writer[P]]
      with api.Writer[Writer[P], P, V]
  {
    //
    //
    //
    val messageProducer: MessageStream#Writer
      = getMessageStream(id).getWriter

    /**
      *
      * @param consumer
      * @tparam C
      * @return
      */
    def getReader[C](
      consumer: C
    ): Try[Reader[C]] = {
      Success(
        new Reader[C](consumer)
      )
    }

    override def getSelf: Writer[P] = this

    /**
      *
      * @param v
      * @return
      */
    def write(
      v: V
    ): Try[Boolean] = {
      val p = new Packet[V](this, reader, v)
      messageProducer.write(p)
    }
  }

  /**
    *
    * @param writer
    * @param reader
    * @param v
    * @tparam V
    */
  class Packet[V](
    writer: Writer[_],
    reader: Reader[_],
    v: V
  ) extends Message[Writer[_], Reader[_], V](
    writer,
    reader,
    v
  )

  /**
    *
    */
  class MessageStream
    extends StreamImpl[Packet[V]] {

    class Reader
      extends super.Reader

    class Writer
      extends super.Writer

    override def getReader(): Reader = new Reader

    override def getWriter(): Writer = new Writer
  }

  //
  //  The identifier last allocated
  //
  var lastAllocatedId: Int = 0x00

  //
  //
  //
  val idMap: mutable.Map[Int, api.Actor[_]]
    = mutable.Map[Int, api.Actor[_]]()

  //
  //
  //
  val messageStreamMap: mutable.Map[Int, MessageStream]
    = mutable.Map[Int, MessageStream]()

  /**
    *
    * @param actor
    * @return
    */
  def allocateId(
    actor: api.Actor[_]
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
    * @return
    */
  def getMessageStream(
    id: Int
  ): MessageStream = {
    messageStreamMap.get(id) match {
      case Some(messageStream: MessageStream) =>
        messageStream

      case None =>
        val messageStream: MessageStream
          = new MessageStream()
        messageStreamMap.put(id, messageStream)
        messageStream
    }
  }

  /**
    *
    * @return
    */
  def getReader[C](
    consumer: C
  ): Reader[C] = {
    new Reader[C](consumer)
  }

  /**
    *
    * @return
    */
  def getWriter[P](
    producer: P,
    reader: Reader[_]
  ): Writer[P] = {
    val messageStream = messageStreamMap.get(reader.getId) match {
      case Some(messageStream: MessageStream) =>
        messageStream

      case None =>
        val messageStream: MessageStream = new MessageStream()
        messageStreamMap.put(reader.getId, messageStream)

    }

    new Writer[P](producer, reader)
  }
}
