package com.resolvix.ohm.module.endpoint.api

/**
  * Defines an endpoint variant module that accepts an input and generates an
  * output in the form of a [[com.resolvix.ohm.module.endpoint.api.EndpointResult]] derivative.
  *
  * @tparam I
  *   the input data type
  *
  * @tparam O
  *   the output data type being a data type that implements the [[com.resolvix.ohm.module.endpoint.api.EndpointResult]]
  *   trait
  */
trait Endpoint[I, O, R <: EndpointResult]
  extends com.resolvix.ohm.module.api.Module[I, O, R]
{

}
