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
class ConsumerPipe[C <: api.Actor[C, P, _, V], P <: api.Actor[P, C, _, V], V](
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
