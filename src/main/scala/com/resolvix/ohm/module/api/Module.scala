package com.resolvix.ohm.module.api

import java.util.Properties

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.typesafe.config.Config

import scala.util.Try

/**
  * The module API specification that provides methods for module
  * interrogation and instantiation.
  *
  * @tparam I
  *   refers to the type of object to be consumed by an instance of
  *   the module.
  *
  * @tparam O
  *   refers to the type of object to be produced by the module in
  *   response to consumption of an object.
  *
  */
trait Module[I, O]
{
  /**
    *
    * @return
    */
  def getDescription: String

  /**
    *
    * @return
    */
  def getHandle: String

  /**
    *
    * @return
    */
  def getId: Int

  /**
    *
    * @return
    */
  def getInstance(): Try[Instance[I, O]]

  /**
    *
    * @param config
    * @return
    */
  def getInstance(
    config: Config
  ): Try[Instance[I, O]]

  /**
    *
    * @param configuration
    * @return
    */
  def getInstance(
    configuration: Map[String, Any]
  ): Try[Instance[I, O]]

  /**
    *
    * @param properties
    * @return
    */
  def getInstance(
    properties: Properties
  ): Try[Instance[I, O]]

  /**
    *
    * @return
    */
  def getName: String
}
