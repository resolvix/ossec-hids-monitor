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
    *
    * @return
    */
  def getId: Int

  /**
    *
    * @return
    */
  def getModule(): Try[Module[I, O, R]]

  /**
    *
    * @param config
    * @return
    */
  def getModule(
    config: Config
  ): Try[Module[I, O, R]]

  /**
    *
    * @param configuration
    * @return
    */
  def getModule(
    configuration: Map[String, Any]
  ): Try[Module[I, O, R]]

  /**
    *
    * @param properties
    * @return
    */
  def getModule(
    properties: Properties
  ): Try[Module[I, O, R]]

  /**
    *
    * @return
    */
  def getName: String
}
