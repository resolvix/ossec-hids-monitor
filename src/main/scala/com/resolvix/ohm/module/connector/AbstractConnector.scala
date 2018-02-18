package com.resolvix.ohm.module

import scala.util.Try

import com.resolvix.ohm.module.api.{Module, Result}

package connector {

  abstract class AbstractConnector[IM <: Module[_, O, R], OM <: Module[O, _, R], O, R <: Result](
    //
    //
    //
    val inputModule: IM,

    //
    //
    //
    val outputModule: OM
  ) extends api.Connector[IM, OM, O, R] {

    override def send(output: O): Try[Boolean] = ???

    override def receive(result: R): Try[Boolean] = ???

  }
}
