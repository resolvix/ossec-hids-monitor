package com.resolvix.concurrent

import java.util.concurrent.TimeUnit

import scala.util.{Failure, Success, Try}

class ConsumerPipe[C <: api.Actor[C, P, V], P <: api.Actor[P, C, V], V](
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
