package com.resolvix.ohm.module.api

import scala.util.Try

/**
  * Defines a runtime instance of a module,
  *
  */
trait Instance[I, O]
{
  /**
    *
    */
  def close(): Try[Boolean]

  /**
    * Consume and object of type 'I', and invoke the function give by 'out'
    * as appropriate.
    *
    * @param in
    *   an object representing the input to the module.
    *
    * @param out
    *   the function through which one or more objects of type 'O' should be
    *   directed in response.
    *
    * @return
    *   a value of type 'Try[Boolean]' indicating whether the operation was
    *   successful or otherwise.
    *
    */
  def consume(
    in: I,
    out: O => Unit
  ): Try[Boolean]

  /**
    * Flush any buffers maintained by the instance in the course of processing
    * objects of type I, and invoke the function given by 'out' as appropriate.
    *
    * @param out
    *    the function through which one or more objects of type 'O' should be
    *    directed.
    *
    * @return
    *    a value of type 'Try[Boolean]' indicating whether the operation was
    *    successful or otherwise.
    *
    */
  def flush(
    out: O => Unit
  ): Try[Boolean]

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
  def getModule: Module[I, O]

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
