package com.resolvix.ohm.module

import java.util.Properties

import com.resolvix.ohm.module.api.{Alert, Instance, Result}
import com.typesafe.config.{Config, ConfigValue}

import scala.util.Try

object AbstractModule
{

  /**
    * Provides an abstract implementation of a module instance.
    *
    * @tparam AI
    *   refers to the relevant 'AbstractInstance' -derived subclass.
    *
    * @tparam I
    *   refers to the type of alert consumed by the module
    *
    * @tparam O
    *   refers to the type of result produced by the module.
    *
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, I, O], I, O]
    extends com.resolvix.ohm.module.api.Instance[I, O]
  {

  }
}

/**
  *
  */
abstract class AbstractModule[I, O]
  extends api.Module[I, O]
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
  protected def newInstance(
    config: Map[String, Any]
  ): Try[Instance[I, O]]

  /**
    *
    * @return
    */
  override def getInstance(): Try[Instance[I, O]] = {
    newInstance(
      Map[String, Any]()
    )
  }

  /**
    *
    * @param config
    * @return
    */
  override def getInstance(
    config: Config
  ): Try[Instance[I, O]] = {
    newInstance(
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
  override def getInstance(
    config: Map[String, Any]
  ): Try[Instance[I, O]] = {
    newInstance(config)
  }

  /**
    *
    * @param properties
    * @return
    */
  override def getInstance(
    properties: Properties
  ): Try[Instance[I, O]] = {
    newInstance(
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
