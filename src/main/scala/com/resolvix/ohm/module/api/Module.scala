package com.resolvix.ohm.module.api

import scala.util.Try

/**
  * Defines a generic form of module for use as part of a modular data pipeline.
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
    *
    */
  def close(): Try[Boolean]

  /**
    * Consumes an object of type I, and return an object of type R.
    *
    * @param input
    *   an object representing the input to the module.
    *
    * @return
    *   a value of type [[Try]] indicating whether the operation was
    *   successful or otherwise.
    */
  def process(
    input: I
  ): Try[R]

  /**
    * Flushes any buffers maintained by the instance in the course of
    * processing objects of type I, and return an object that implements the
    * [[Result]] trait.
    *
    * @return
    *    a value of type [[Try]] indicating whether the operation was
    *    successful or otherwise.
    */
  def flush(): Try[R]

  /**
    *
    * @return
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
    *
    * @return
    */
  def initialise(): Try[Boolean]

  /**
    * Instantiate the consumer for the module.
    *
    */
  def open(): Try[Boolean]

  /**
    * Terminate the consumer for the module.
    *
    * @return
    */
  def terminate(): Try[Boolean]
}
