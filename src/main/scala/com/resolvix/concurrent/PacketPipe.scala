package com.resolvix.concurrent

class PacketPipe[D <: api.Actor[D, S, V], S <: api.Actor[S, D, V], V]
  extends Pipe[Packet[S, D, V]]
