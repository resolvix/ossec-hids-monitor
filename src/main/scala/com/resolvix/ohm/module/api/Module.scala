package com.resolvix.ohm.module.api

import scala.util.Try

import com.resolvix.ohm.module.api.ConnectorLocation._

/**
  * Defines a generic form of module for use as part of a modular data
  * transformation and processing pipeline.
  *
  * @tparam I
  *   the input data type
  *
  * @tparam O
  *   the output data type
  *
  * @tparam R
  *   the result data type being a data type that implements the [[Result]]
  *   trait
  */
trait Module[I, O, R <: Result] {

  /**
    * Signals that the application intends to stop making calls to the
    * 'consume' method of the module.
    *
    * @return
    *   a value of type [[Try[[Boolean]]] indicating whether the operation was
    *   successful or not
    */
  def close(): Try[Boolean]

  /**
    * Establishes a connection between this module and other modules, with
    * implicit determination of whether the connector is to be connected as
    * an input to the module, or as an output to the module.
    *
    * @param connector
    *   the connector
    *
    * @tparam OO
    *   the output type
    *
    * @tparam RR
    *   the result type
    *
    */
  def connect[OO, RR <: Result](connector: Connector[Module[_, OO, RR], Module[OO, _, RR], OO, RR]): Try[Boolean]

  /**
    * Establishes a connection between this module and other modules, with explici
    *
    * @param location
    *   the connector location
    *
    * @param connector
    *   the connector
    *
    * @tparam OO
    *   the output type
    *
    * @tparam RR
    *   the result type
    *
    */
  def connect[OO, RR <: Result](
    location: ConnectorLocation,
    connector: Connector[Module[_, _, _], Module[_, _, _], OO, RR]): Try[Boolean]

  /**
    * Consumes an input object of type [[I]], and returns an object of type
    * [[Try[Boolean]]] indicating whether the operation had been successful
    * and, if not, the reason for the failure.
    *
    * @param input
    *   the input object of type [[I]]
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating whether the operation
    *   was successful or not
    */
  def consume(input: I): Try[Boolean]

  /**
    * Consumes a result object of type [[R]], and returns an object of type
    * [[Try[Boolean]]] indicating whether the operation had been successful
    * and, if not, the reason for the failure.
    *
    * @param result
    *   the result object of type [[R]]
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating whether the operation
    *   was successful or not
    */
  def consume(result: R): Try[Boolean]

  /**
    * Flushes any buffers maintained by the instance in the course of
    * processing objects of type I, and return an object that implements the
    * [[Result]] trait.
    *
    * @return
    *   a value of type [[Try[R]]] indicating whether the operation was
    *   successful or not
    */
  def flush(): Try[R]

  /**
    * Returns the unique identifier for the module instance.
    *
    * @return
    *   the unique identifier
    */
  def getId: Int

  /**
    * Returns the [[ModuleDescriptor]] for the module instance.
    *
    * @return
    *    the [[ModuleDescriptor]]
    */
  def getDescriptor: ModuleDescriptor[I, O, R]

  /**
    * Initialises the module.
    *
    * @return
    */
  def initialise(): Try[Boolean];

  /**
    * Signals that the application intends to begin making calls to the
    * 'consume' method of the module.
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating whether the open operation
    *   was successful or not
    */
  def open(): Try[Boolean]

  /**
    * Terminate the consumer for the module.
    *
    * @return
    */
  def terminate(): Try[Boolean]
}
