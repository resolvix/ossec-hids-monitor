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
  class Reader[C](
    val consumer: C
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
    ): Try[Writer[P, C]] = {
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
    def read[W <: api.Writer[W, P, V], P]: Try[(Int, V)] = {
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
    def read[P](
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
  class Writer[P, C](
    val producer: P,
    val consumer: C
  ) extends Actor[Writer[P, C]]
      with api.Writer[Writer[P, C], P, V]
  {
    //
    //
    //
    val reader: Reader[C]
      = getReader(consumer) match {
        case Success(r: Reader[C]) => r
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
      * @tparam C
      * @return
      */
    def getReader[C](
      consumer: C
    ): Try[Reader[C]] = {
      Success(
        MessageQueue.this.getReader(
          consumer
        )
      )
    }

    override def getSelf: Writer[P, C] = this

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
  def getReader[R <: api.Reader[R, C, V], C, V](
    consumer: C
  ): R = {
    idMap.find({
      case (i: Int, r: Reader[C @unchecked]) => r.consumer.equals(consumer)
      case _ => false
    }) match {
      case Some((i: Int, a: api.Actor[_])) =>
        a.asInstanceOf[R]

      case _ =>
        val newReader: Reader[C] = new Reader[C](consumer)
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
  def getWriter[W <: api.Writer[W, P, V], R <: api.Reader[R, C, V], P, C, V](
    producer: P,
    consumer: C
  ): W = {
    idMap.find({
      case (i: Int, w: Writer[P @unchecked, C @unchecked]) =>
        w.producer.equals(producer) && w.consumer.equals(consumer)

      case _ => false
    }) match {
      case Some((i: Int, a: api.Actor[_])) =>
        a.asInstanceOf[W]

      case _ =>
        val newWriter: Writer[P, C] = new Writer[P, C](producer, consumer)
        idMap.put(newWriter.getId, newWriter)
        newWriter.asInstanceOf[W]
    }
  }
}
