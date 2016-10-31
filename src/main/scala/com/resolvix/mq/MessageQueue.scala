package com.resolvix.mq

import com.sun.xml.internal.ws.api.server.LazyMOMProvider.DefaultScopeChangeListener

import scala.collection._
import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * The PacketPipe implements a multipoint-to-multippoint message queue
  * intended to support situations where multiple producers are able to
  * send messages to a multiple consumers.
  *
  * @tparam S
  *   refers to the type of the Producer intended to produce messages of
  *   type V
  *
  * @tparam D
  *   refers to the type of the Consumer intended to consume messages of
  *   type V
  *
  * @tparam V
  *   refers to the type of messages intended to be produced and consumed
  *   through this instant packet pipe.
  */
class MessageQueue[V] {

  //
  //
  //
  type PacketV = Message[V]

  /**
    *
    */
  class Consumer[P <: api.Identifiable, C <: api.Identifiable](
    consumer: C
  ) extends api.Pipe[V]#Consumer {

    //
    //
    //
    val messageConsumer: MessagePipe[api.Identifiable, api.Identifiable]#Consumer
      = getMessagePipe(consumer.getId).getConsumer

    /**
      *
      * @param producer
      * @return
      */
    def getProducer[P <: api.Identifiable](
      producer: P
    ): Try[api.Pipe[V]#Producer] = {
      Success(
        new Producer(consumer, producer)
      )
    }

    /**
      *
      * @return
      */
    override def read: Try[(P, V)] = {
      messageConsumer.read match {
        case Success(p: Message[P, C, V]) =>
          Success(
            (p.getSource.asInstanceOf[P], p.getV)
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
    override def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[(P, V)] = {
      messageConsumer.read(timeout, unit) match {
        case Success(p: Message[P, C, V]) =>
          Success(
            (p.getSource.asInstanceOf[P], p.getV)
          )

        case Failure(t: Throwable) =>
          Failure(t)
      }
    }
  }

  /**
    *
    * @param source
    */
  class Producer[
    S <: api.Identifiable,
    D <: api.Identifiable
  ] (
    source: S,
    destination: D
  ) extends api.Pipe[V]#Producer {

    //
    //
    //
    val messageProducer: MessagePipe[api.Identifiable, api.Identifiable]#Producer
      = getMessagePipe(destination.getId).getProducer

    /**
      *
      * @param destination
      * @return
      */
    def getConsumer(
      destination: D
    ): Try[MessagePipe[api.Identifiable, api.Identifiable]#Consumer] = {
      Success(
        new Consumer(destination)
      )
    }

    /**
      *
      * @param v
      * @return
      */
    override def write(
      v: V
    ): Try[Boolean] = {
      val p = new Message[api.Identifiable, api.Identifiable, V](source, destination, v)
      messageProducer.write(p)
    }
  }

  /**
    *
    */
  class MessagePipe[
    S <: api.Identifiable,
    D <: api.Identifiable
  ] extends Pipe[Message[S, D, V]] {

    class Consumer
      extends Pipe[Message[S, D, V]]#Consumer

    class Producer
      extends Pipe[Message[S, D, V]]#Producer

    def getConsumer: Consumer = new Consumer

    def getProducer: Producer = new Producer

  }

  //
  //
  //
  val messagePipeMap: mutable.Map[Int, MessagePipe[api.Identifiable, api.Identifiable]]
    = mutable.Map[Int, MessagePipe[api.Identifiable, api.Identifiable]]()

  /**
    *
    * @param id
    * @tparam I
    * @return
    */
  def getMessagePipe[I <: api.Identifiable](
    id: Int
  ): MessagePipe[api.Identifiable, api.Identifiable] = {
    messagePipeMap.get(id) match {
      case Some(messagePipe: MessagePipe[api.Identifiable, api.Identifiable]) =>
        messagePipe

      case None =>
        val messagePipe: MessagePipe[api.Identifiable, api.Identifiable]
          = new MessagePipe[api.Identifiable, api.Identifiable]()
        messagePipeMap.put(id, messagePipe)
        messagePipe
    }
  }

  /**
    *
    * @return
    */
  def getConsumer[
    S <: api.Identifiable,
    D <: api.Identifiable
  ] (
    source: S,
    destination: D
  ): Consumer[S, D] = {
    val messagePipe =

    new Consumer(destination)
  }

  /**
    *
    * @return
    */
  def getProducer[
  S <: api.Identifiable,
  D <: api.Identifiable
  ] (
    source: S,
    destination: D
  ): Producer[S, D] = {
    val messagePipe = messagePipeMap.get(destination.getId) match {
      case Some(messagePipe: MessagePipe[S, D]) =>
        messagePipe

      case None =>
        val messagePipe: MessagePipe[api.Identifiable, api.Identifiable]
          = new MessagePipe[api.Identifiable, api.Identifiable]()
        messagePipeMap.put(destination.getId, messagePipe)

    }

    new Producer(source, destination)
  }
}
