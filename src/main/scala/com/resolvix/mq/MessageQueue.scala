package com.resolvix.mq

import com.resolvix.sio.StreamImpl

import scala.collection._
import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * MessageQueue implements a multipoint-to-multipoint message queue
  * intended to support situations where multiple producers are able to
  * send messages to a multiple consumers.
  *
  * @tparam V
  *   refers to the type of messages intended to be written to and read
  *   from the relevant message queue.
  */
class MessageQueue[V]
  extends api.MessageQueue[V]
{

  abstract class Actor
    extends api.Actor
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
    def getSelf: Actor
  }

  /**
    * Objects of type Reader[C] are intended to provide an interface
    * between the ultimate consumer and the message stream for messages
    * of type V.
    *
    * The Reader class enables the consumer to ascertain which
    * counterparty the message came from.
    *
    * @tparam R
    *   specifies the type of the identifier for Consumer objects intended
    *   to consume messages of type V
    *
    * @tparam W
    */
  class Reader(
    val consumer: Any
  ) extends Actor
      with api.Reader[V] {

    //
    //  Obtain a Reader object for the underlying message stream.
    //
    val messageConsumer: MessageStream#Reader
      = getMessageStream(getId).getReader


    /**
      *
      * @return
      */
    override def getSelf: Reader = this

    /**
      *
      * @param producer
      * @return
      */
    def getWriter(
      producer: Any
    ): Try[Writer] = {
      Success(
        MessageQueue.this.getWriter(
          producer,
          consumer
        )
      )
    }

    /**
      *
      * @return
      */
    def read: Try[(Int, V)] = {
      messageConsumer.read match {
        case Success(p: Packet[V]) =>
          Success(
            (p.getWriter, p.getV)
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
    def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[(Int, V)] = {
      messageConsumer.read(timeout, unit) match {
        case Success(p: Packet[V]) =>
          Success(
            (p.getWriter, p.getV)
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
    */
  class Writer(
    val producer: Any,
    val consumer: Any
  ) extends Actor
      with api.Writer[V]
  {
    //
    //
    //
    val reader: Reader
      = getReader(consumer) match {
        case Success(r: Reader) => r
        case Failure(t: Throwable) => throw t
      }

    //
    //
    //
    val messageWriter: MessageStream#Writer
      = getMessageStream(reader.getId).getWriter

    /**
      *
      * @param consumer
      * @return
      */
    def getReader(
      consumer: Any
    ): Try[Reader] = {
      Success(
        MessageQueue.this.getReader(
          consumer
        )
      )
    }


    /**
      *
      * @return
      */
    override def getSelf: Writer = this

    /**
      *
      * @param v
      * @return
      */
    def write(
      v: V
    ): Try[Boolean] = {
      val p = new Packet[V](this.getId, reader.getId, v)
      messageWriter.write(p)
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
    writer: Int,
    reader: Int,
    v: V
  ) extends Message[Int, Int, V](
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

    override def getReader: Reader = new Reader

    override def getWriter: Writer = new Writer
  }

  //
  //  The identifier last allocated
  //
  var lastAllocatedId: Int = 0x00

  //
  //
  //
  val idMap: mutable.Map[Int, api.Actor]
    = mutable.Map[Int, api.Actor]()

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
  def getReader[R <: api.Reader[V], V](
    consumer: Any
  ): R = {
    idMap.find({
      case (i: Int, r: Reader) => r.consumer.equals(consumer)
      case _ => false
    }) match {
      case Some((i: Int, a: api.Actor)) =>
        a.asInstanceOf[R]

      case _ =>
        val newReader: Reader = new Reader(consumer)
        idMap.put(newReader.getId, newReader)
        newReader.asInstanceOf[R]
    }
  }

  /*def newMessage(
    reader: Reader[C]
  ): Unit = {
    val messageStream = messageStreamMap.get(reader.getId) match {
      case Some(messageStream: MessageStream) =>
        messageStream

      case None =>
        val messageStream: MessageStream = new MessageStream()
        messageStreamMap.put(reader.getId, messageStream)

    }
  }*/

  /**
    *
    * @return
    */
  def getWriter[W <: api.Writer[V], R <: api.Reader[V], V](
    producer: Any,
    consumer: Any
  ): W = {
    idMap.find({
      case (i: Int, w: Writer) =>
        w.producer.equals(producer) && w.consumer.equals(consumer)

      case _ => false
    }) match {
      case Some((i: Int, a: api.Actor)) =>
        a.asInstanceOf[W]

      case _ =>
        val newWriter: Writer = new Writer(producer, consumer)
        idMap.put(newWriter.getId, newWriter)
        newWriter.asInstanceOf[W]
    }
  }
}
