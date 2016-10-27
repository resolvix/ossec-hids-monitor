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
  C <: api.Actor[C, CT, P, PT, V],
  CT <: api.Transport[V],
  P <: api.Actor[P, PT, C, CT, V],
  PT <: api.Transport[V],
  V
](
  packetPipe: PacketPipe[C, CT, P, PT, V]
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
      case Success(pV: Packet[P, PT, C, CT, V]) =>
        Success(pV.getV)

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }
}
