package com.resolvix.concurrent

class PacketPipe[D <: api.Actor[D, S, _, V], S <: api.Actor[S, D, _, V], V]
  extends Pipe[Packet[S, D, V]]
