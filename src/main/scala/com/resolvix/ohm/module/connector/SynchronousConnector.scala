package com.resolvix.ohm.module

import com.resolvix.ohm.module.api.{Module, Result}

import scala.util.Try

package connector {

  class SynchronousConnector[IM <: Module[_, O, R], OM <: Module[O, _, R], O, R <: Result](
    //
    //
    //
    inputModule: IM,

    //
    //
    //
    outputModule: OM
  ) extends AbstractConnector[IM, OM, O, R](inputModule, outputModule)
    with api.Connector[IM, OM, O, R] {

    override def send(output: O): Try[Boolean] = {
      outputModule.consume(output)
    }

    override def receive(result: R): Try[Boolean] = {
      inputModule.consume(result)
    }
  }
}
