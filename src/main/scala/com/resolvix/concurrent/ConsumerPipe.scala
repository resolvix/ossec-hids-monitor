package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import scala.util.{Failure, Success, Try}

/**
  * A ConsumerPipe is a Pipe viewed from the perspective of a Consumer.
  *
  * @param packetPipe
  * @tparam C
  * @tparam P
  * @tparam T
  * @tparam V
  */
class ConsumerPipe[
  C <: api.Actor[C, ConsumerPipe[C, P, V], P, ProducerPipe[C, P, V], V],
  P <: api.Actor[P, ProducerPipe[P, C, V], C, ConsumerPipe[C, P, V], V],
  V
](
  packetPipe: PacketPipe[C, P, V]
) extends api.ConsumerPipe[V] {

  override def read: Try[V] = {
    packetPipe.read match {
      case Success(pV: V @unchecked) =>
        Success(pV.getV)

      case Success(_) =>
        throw new IllegalStateException()

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  override def read(
    timeout: Int,
    unit: TimeUnit
  ): Try[V] = {
    packetPipe.read(
      timeout,
      unit
    ) match {
      case Success(pV: Packet[P, C, V]) =>
        Success(pV.getV)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }
}
