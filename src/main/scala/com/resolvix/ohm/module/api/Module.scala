package com.resolvix.ohm.module.api

import scala.util.Try

trait Module[I, O]
{
  /**
    *
    */
  def close(): Try[Boolean]

  /**
    * Consume and object of type 'I', and return an object that implements
    * the 'Result' trait.
    *
    * @param input
    *   an object representing the input to the module.
    *
    * @tparam R
    *   the result type.
    *
    * @return
    *   a value of type 'Try[R]' indicating whether the operation was
    *   successful or otherwise.
    *
    */
  def process[R <: Result](
    input: I
  ): Try[R]

  /**
    * Flush any buffers maintained by the instance in the course of processing
    * objects of type I, and return an object that implements the 'Result' trait.
    *
    * @tparam R
    *   the result type.
    *
    * @return
    *    a value of type 'Try[R]' indicating whether the operation was
    *    successful or otherwise.
    *
    */
  def flush[R <: Result](): Try[R]

  /**
    *
    * @return
    */
  def getId: Int

  /**
    * Returns the parent 'Module' for the module instance.
    *
    * @return
    */
  def getModule: ModuleDescriptor[I, O]

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
