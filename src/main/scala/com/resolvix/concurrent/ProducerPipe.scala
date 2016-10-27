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
  P <: api.Actor[P, PT, C, CT, V],
  PT <: api.Transport[V],
  C <: api.Actor[C, CT, P, PT, V],
  CT <: api.Transport[V],
  V
](
  producer: P,
  producerPacketPipes: Map[Int, api.ProducerPipe[V]]
) extends api.ProducerPipe[V] {

  override def write(
    v: V
  ): Try[Boolean] = {
    try {
      val p: Packet[P, PT, C, CT, V] = new Packet[P, PT, C, CT, V](producer, v)
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
