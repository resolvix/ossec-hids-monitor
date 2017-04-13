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
    * @tparam A
    *   refers to the type of alert consumed by the module
    *
    * @tparam R
    *   refers to the type of result produced by the module.
    *
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, A, R], A <: Alert, R <: Result]
    extends com.resolvix.ohm.module.api.Instance[A, R]
  {

    /**
      * Abstract class definition for the module consumer / producer.
      */
    abstract class ConsumerProducer
      extends  com.resolvix.ccs.runnable.ConsumerProducer[ConsumerProducer, A, R]
    {
      override def doConsume(c: A): Try[Boolean]

      override def doProduce(): Try[R]
    }
  }
}

/**
  *
  */
abstract class AbstractModule[A <: Alert, R <: Result]
  extends api.Module[A, R]
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
  ): Try[Instance[A, R]]

  /**
    *
    * @return
    */
  override def getInstance(): Try[Instance[A, R]] = {
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
  ): Try[Instance[A, R]] = {
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
  ): Try[Instance[A, R]] = {
    newInstance(config)
  }

  /**
    *
    * @param properties
    * @return
    */
  override def getInstance(
    properties: Properties
  ): Try[Instance[A, R]] = {
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
