package com.resolvix.concurrent

class PacketPipe[
  D <: api.Actor[D, DT, S, ST, V],
  DT <: api.Transport[V],
  S <: api.Actor[S, ST, D, DT, V],
  ST <: api.Transport[V],
  V
] extends Pipe[Packet[S, ST, D, DT, V]]
