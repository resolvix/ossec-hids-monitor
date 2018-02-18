package com.resolvix.ohm.module

import com.resolvix.ohm.module.api.{Module, Result}

package connector {

  import scala.util.Try

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
