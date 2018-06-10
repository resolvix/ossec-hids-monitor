package com.resolvix.ohm.module.api

import com.resolvix.ccs.api.{Consumer, Producer}

import scala.util.Try

/**
  * Defines a generic form of connector for use as part of a modular data
  * transformation and processing platform.
  *
  * @tparam IM
  *   the input module interface specification
  *
  * @tparam OM
  *   the output module interface specification
  *
  * @tparam O
  *   the output type emitted by the input module, and accepted for input by
  *   the output module
  *
  * @tparam R
  *   the result type emitted by the output module, and accepted by the input
  *   module for onward transmission to the application mainframe
  *
  */
trait Connector[IM <: Module[_, O, R], OM <: Module[O, _, R], O, R <: Result] {

  /**
    * Transmits an object of the output type emitted by the input module, to
    * the output module.
    *
    * @param output
    *   the object
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating the success or failure of
    *   the operation
    */
  def send(output: O): Try[Boolean]

  /**
    * Transmits an object of the result type emitted by the output module, to
    * the input module.
    *
    * @param result
    *   the result
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating the success or failure of
    *   the operation
    *
    */
  def receive(result: R): Try[Boolean]
}
