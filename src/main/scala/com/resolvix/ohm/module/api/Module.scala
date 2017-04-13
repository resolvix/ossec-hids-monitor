package com.resolvix.ohm.module.api

import java.util.Properties

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.typesafe.config.Config

import scala.util.Try

/**
  * The module API specification that provides methods for module
  * interrogation and instantiation.
  *
  * @tparam A
  *   refers to the type of alert to be consumed by the module.
  *
  * @tparam R
  *   refers to the type of module alert status to be produced by the module.
  *
  */
trait Module[A <: Alert, R <: Result]
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
  def getInstance(): Try[Instance[A, R]]

  /**
    *
    * @param config
    * @return
    */
  def getInstance(
    config: Config
  ): Try[Instance[A, R]]

  /**
    *
    * @param configuration
    * @return
    */
  def getInstance(
    configuration: Map[String, Any]
  ): Try[Instance[A, R]]

  /**
    *
    * @param properties
    * @return
    */
  def getInstance(
    properties: Properties
  ): Try[Instance[A, R]]

  /**
    *
    * @return
    */
  def getName: String
}
