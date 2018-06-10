package com.resolvix.ohm.module.api

import java.util.Properties

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
trait ModuleDescriptor[I, O, R <: Result]
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
    * Returns the unique identifier for the module.
    *
    * @return
    *   the unique identifier
    */
  def getId: Int

  /**
    * Returns the [[Module]] described by the instant [[ModuleDescriptor]].
    *
    * @return
    *   the [[Module]]
    */
  def getModule(): Try[Module[I, O, R]]

  /**
    * Returns a new configured instance of the module.
    *
    * @param config
    *   the configuration for the [[Module]] instance
    *
    * @return
    *   the new configured [[Module]]
    */
  def getModule(
    config: Config
  ): Try[Module[I, O, R]]

  /**
    * Returns a new configured instance of the module.
    *
    * @param configuration
    *   the configuration for the [[Module]] instance
    *
    * @return
    *   the new configured [[Module]]
    */
  def getModule(
    configuration: Map[String, Any]
  ): Try[Module[I, O, R]]

  /**
    * Returns a new configured instance of the module.
    *
    * @param properties
    *   the configuration for the [[Module]] instance
    *
    * @return
    *   the new configured [[Module]]
    */
  def getModule(
    properties: Properties
  ): Try[Module[I, O, R]]

  /**
    * Returns the name of the module.
    *
    * @return
    *   the name of the module
    */
  def getName: String
}
