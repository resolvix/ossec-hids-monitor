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
class MessageQueue[V]
  extends api.MessageQueue[V]
{

  /**
    * Objects of type Consumer[C] are intended to provide an interface
    * between the message stream for messages of type V that enables the
    * consumer to identify the producer of the message.
    *
    * @tparam C
    *   specifies the type of the Consumer intended to consume messages of
    *   type V
    */
  class Consumer[C <: api.Actor, P <: api.Actor](
    consumer: C
  ) extends super.Consumer[C, P] {

    //
    //
    //
    val messageConsumer: MessageStream#Consumer
      = getMessageStream(consumer.getId).getConsumer

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
        case Success(p: Packet[V]) =>
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
        case Success(p: Packet[V]) =>
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
    P <: api.Actor,
    C <: api.Actor
  ] (
    producer: P,
    consumer: C
  ) extends super.Producer[P, C] {

    //
    //
    //
    val messageProducer: MessageStream#ProducerV
      = getMessageStream(consumer.getId).getProducer

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
      val p = new Packet[V](producer, consumer, v)
      messageProducer.write(p)
    }
  }

  class Packet[V](
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
  class MessageStream
    extends StreamImpl[Packet[V]] {

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
  val messageStreamMap: mutable.Map[Int, MessageStream]
    = mutable.Map[Int, MessageStream]()

  /**
    *
    * @param id
    * @tparam I
    * @return
    */
  def getMessageStream[I <: api.Identifiable](
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
  def getConsumer[
    P <: api.Actor,
    C <: api.Actor
  ] (
    consumer: C
  ): Consumer[C, P] = {
    new Consumer(consumer)
  }

  /**
    *
    * @return
    */
  def getProducer[
    P <: api.Actor,
    C <: api.Actor
  ] (
    producer: P,
    consumer: C
  ): Producer[P, C] = {
    val messageStream = messageStreamMap.get(consumer.getId) match {
      case Some(messageStream: MessageStream) =>
        messageStream

      case None =>
        val messageStream: MessageStream = new MessageStream()
        messageStreamMap.put(consumer.getId, messageStream)

    }

    new Producer(producer, consumer)
  }
}
