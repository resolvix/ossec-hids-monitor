package com.resolvix.mq

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

  /**
    * The objects of type Consumer[C] is intended to provide an interface
    * between the message pipe for messages of type V that enables the consumer
    * to identify the producer of the message.
    *
    * @tparam C
    *   specifies the type of the Consumer intended to consume messages of
    *   type V
    */
  class Consumer[C <: api.Identifiable, P <: api.Identifiable](
    consumer: C
  ) extends api.Consumer[V] {

    //
    //
    //
    val messageConsumer: MessagePipe#Consumer
      = getMessagePipe(consumer.getId).getConsumer

    /**
      *
      * @param producer
      * @return
      */
    def getProducer(
      producer: P
    ): Try[Producer[P, C]] = {
      Success(
        new Producer(producer, consumer)
      )
    }

    /**
      *
      * @return
      */
    def read: Try[(P, V)] = {
      messageConsumer.read match {
        case Success(p: MessageWrapper[V]) =>
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
    def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[(P, V)] = {
      messageConsumer.read(timeout, unit) match {
        case Success(p: MessageWrapper[V]) =>
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
    * @param producer
    *
    * @param consumer
    */
  class Producer[
    P <: api.Identifiable,
    C <: api.Identifiable
  ] (
    producer: P,
    consumer: C
  ) extends api.Producer[V] {

    //
    //
    //
    val messageProducer: MessagePipe#ProducerV
      = getMessagePipe(consumer.getId).getProducer

    /**
      *
      * @param consumer
      * @return
      */
    def getConsumer(
      consumer: C
    ): Try[Consumer[C, P]] = {
      Success(
        new Consumer[C, P](consumer)
      )
    }

    /**
      *
      * @param v
      * @return
      */
    def write(
      v: V
    ): Try[Boolean] = {
      val p = new MessageWrapper[V](producer, consumer, v)
      messageProducer.write(p)
    }
  }

  class MessageWrapper[V](
    producer: api.Identifiable,
    consumer: api.Identifiable,
    v: V
  ) extends Message[api.Identifiable, api.Identifiable, V](
    producer,
    consumer,
    v
  )

  /**
    *
    */
  class MessagePipe
    extends PipeImpl[MessageWrapper[V]] {

    class ConsumerV
      extends Consumer

    class ProducerV
      extends Producer

    override def getConsumer: ConsumerV = new ConsumerV

    override def getProducer: ProducerV = new ProducerV

  }

  //
  //
  //
  val messagePipeMap: mutable.Map[Int, MessagePipe]
    = mutable.Map[Int, MessagePipe]()

  /**
    *
    * @param id
    * @tparam I
    * @return
    */
  def getMessagePipe[I <: api.Identifiable](
    id: Int
  ): MessagePipe = {
    messagePipeMap.get(id) match {
      case Some(messagePipe: MessagePipe) =>
        messagePipe

      case None =>
        val messagePipe: MessagePipe
          = new MessagePipe()
        messagePipeMap.put(id, messagePipe)
        messagePipe
    }
  }

  /**
    *
    * @return
    */
  def getConsumer[
    P <: api.Identifiable,
    C <: api.Identifiable
  ] (
    producer: P,
    consumer: C
  ): Consumer[C, P] = {
    new Consumer(consumer)
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
      case Some(messagePipe: MessagePipe) =>
        messagePipe

      case None =>
        val messagePipe: MessagePipe = new MessagePipe()
        messagePipeMap.put(destination.getId, messagePipe)

    }

    new Producer(source, destination)
  }
}
