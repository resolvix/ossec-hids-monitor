package com.resolvix.concurrentx
import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * The PacketPipe implements a multipoint-to-point packet pipe intended
  * to support situations where multiple producers are able to send messages
  * to a single consumer.
  *
  * @tparam D
  *   refers to the type of the Consumer intended to consume messages of
  *   type V
  *
  * @tparam S
  *   refers to the type of the Producer intended to produce messages of
  *   type V
  *
  * @tparam V
  *   refers to the type of messages intended to be produced and consumed
  *   through this instant packet pipe.
  */
class PacketPipe[
  D <: api.Actor[D, S, V],
  S <: api.Actor[S, D, V],
  V
] {

  //
  //
  //
  type PacketV = Packet[S, D, V]

  /**
    *
    */
  class Consumer(
    destination: D
  ) extends api.Pipe[(S, V)]#Consumer {
    //
    //
    //
    val consumer: api.Pipe[Packet[S, D, V]]#Consumer
      = pipe.getConsumer

    /**
      *
      * @param producer
      * @return
      */
    def getProducer(
      producer: S
    ): Try[Producer] = {
      Success(
        new Producer(producer, destination)
      )
    }

    /**
      *
      * @return
      */
    override def read: Try[(S, V)] = {
      consumer.read match {
        case Success(p: Packet[S, D, V]) =>
          Success(
            (p.getSource, p.getV)
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
    ): Try[(S, V)] = {
      consumer.read(timeout, unit) match {
        case Success(p: Packet[S, D, V]) =>
          Success(
            (p.getSource, p.getV)
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
  class Producer(
    source: S,
    destination: D
  ) extends api.Pipe[V]#Producer {

    //
    //
    //
    val producer: api.Pipe[Packet[S, D, V]]#Producer
      = pipe.getProducer

    /**
      *
      * @param destination
      * @return
      */
    def getConsumer(
      destination: D
    ): Try[Consumer] = {
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
      val p = new Packet[S, D, V](source, destination, v)
      producer.write(p)
    }
  }

  //
  //
  //
  val pipe: Pipe[Packet[S, D, V]] = new Pipe[Packet[S, D, V]]()

  /**
    *
    * @return
    */
  def getConsumer(
    listener: D
  ): Consumer = {
    new Consumer
  }

  /**
    *
    * @return
    */
  def getProducer(
    source: S,
    destination: D
  ): Producer = {
    new Producer(source, destination)
  }
}
