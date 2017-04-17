package com.resolvix.ohm.module

import java.util.Properties

import com.resolvix.ohm.module.api.{Alert, Module, ResultX}
import com.typesafe.config.{Config, ConfigValue}

import scala.util.Try

object AbstractModuleDescriptor
{

}

/**
  *
  */
abstract class AbstractModuleDescriptor[I, O]
  extends api.ModuleDescriptor[I, O]
{
  /**
    *
    * @return
    */
  protected def getConfigurations: Array[String]

  /**
    *
    * @return
    */
  override def getDescription: String = ???

  /**
    *
    * @return
    */
  override def getHandle: String = ???

  /**
    *
    * @return
    */
  override def getId: Int = ???

  /**
    *
    * @param config
    * @return
    */
  protected def newModule(
    config: Map[String, Any]
  ): Try[Module[I, O]]

  /**
    *
    * @return
    */
  override def getModule(): Try[Module[I, O]] = {
    newModule(
      Map[String, Any]()
    )
  }

  /**
    *
    * @param config
    * @return
    */
  override def getModule(
    config: Config
  ): Try[Module[I, O]] = {
    newModule(
      getConfigurations.map(
        (h: String) => config.getValue(h) match {
          case v: ConfigValue =>
            (h, v.unwrapped())
        }
      ).toMap
    )
  }

  /**
    *
    * @param config
    * @return
    */
  override def getModule(
    config: Map[String, Any]
  ): Try[Module[I, O]] = {
    newModule(config)
  }

  /**
    *
    * @param properties
    * @return
    */
  override def getModule(
    properties: Properties
  ): Try[Module[I, O]] = {
    newModule(
      getConfigurations.map(
        (h: String) => properties.get(h) match {
          case a: Any =>
            (h, a)
        }
      ).toMap
    )
  }

  /**
    *
    * @return
    */
  override def getName: String = ???
}
