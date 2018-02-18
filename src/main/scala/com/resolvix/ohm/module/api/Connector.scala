package com.resolvix.ohm.module.api

import com.resolvix.ccs.api.{Consumer, Producer}

import scala.util.Try

trait Connector[IM <: Module[_, O, R], OM <: Module[O, _, R], O, R <: Result] {

  def send(output: O): Try[Boolean]

  def receive(result: R): Try[Boolean]
}
