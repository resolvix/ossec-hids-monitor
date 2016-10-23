package com.resolvix.concurrent
import scala.util.{Failure, Success, Try}

class ProducerPipe[P <: api.Actor[P, C, V], C <: api.Actor[C, P, V], V](
  producer: P,
  consumerPacketPipes: Map[Int, api.ProducerPipe[V]]
) extends api.ProducerPipe[V] {

  override def write(
    v: V
  ): Try[Boolean] = {
    try {
      val p: Packet[P, C, V] = new Packet[P, C, V](producer, v)
      consumerPacketPipes.foreach {
        x: (Int, api.ProducerPipe[V]) => x._2.write(v)
      }
      Success(true)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }
}
