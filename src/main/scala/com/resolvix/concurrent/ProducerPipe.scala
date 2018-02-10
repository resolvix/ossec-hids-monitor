package com.resolvix.concurrent
import scala.util.{Failure, Success, Try}

/**
  * A ProducerPipe is a Pipe viewed from the perspective of a Producer.
  *
  * @param producer
  * @param producerPacketPipes
  * @tparam P
  * @tparam C
  * @tparam V
  */
class ProducerPipe[
  P <: api.Actor[P, ProducerPipe[P, C, V], C, ConsumerPipe[C, P, V], V],
  C <: api.Actor[C, ConsumerPipe[C, P, V], P, ProducerPipe[C, P, V], V],
  V
](
  producer: P,
  producerPacketPipes: Map[Int, api.ProducerPipe[V]]
) extends api.ProducerPipe[V] {

  override def write(
    v: V
  ): Try[Boolean] = {
    try {
      val p: Packet[P, C, V] = new Packet[P, C, V](producer, v)
      producerPacketPipes.foreach {
        x: (Int, api.ProducerPipe[V]) => x._2.write(v)
      }
      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }


}
