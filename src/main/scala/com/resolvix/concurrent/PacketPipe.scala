package com.resolvix.concurrent

class PacketPipe[
  D <: api.Actor[D, _ <: api.Transport[V], S, _ <: api.Transport[V], V],
  S <: api.Actor[S, _ <: api.Transport[V], D, _ <: api.Transport[V], V],
  V
] extends Pipe[Packet[S, D, V]]
