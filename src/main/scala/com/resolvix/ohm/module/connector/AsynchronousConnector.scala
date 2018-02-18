package com.resolvix.ohm.module

package connector {

  import com.resolvix.ohm.module.api.{Module, Result}

  import scala.util.Try

  class AsynchronousConnector[IM <: Module[_, O, R], OM <: Module[O, _, R], O, R <: Result](

  ) extends api.Connector[IM, OM, O, R] {
    override def send(output: O): Try[Boolean] = ???

    override def receive(result: R): Try[Boolean] = ???
  }
}