package com.resolvix.ohm.module.api

import java.util.Properties

import com.typesafe.config.Config

import scala.util.Try

/**
  *
  * @tparam A
  * @tparam M
  */
trait Module[A <: Alert, M <: ModuleAlertStatus]
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
  def getInstance(): Try[Instance[A, M]]

  /**
    *
    * @param config
    * @return
    */
  def getInstance(
    config: Config
  ): Try[Instance[A, M]]

  /**
    *
    * @param configuration
    * @return
    */
  def getInstance(
    configuration: Map[String, Any]
  ): Try[Instance[A, M]]

  /**
    *
    * @param properties
    * @return
    */
  def getInstance(
    properties: Properties
  ): Try[Instance[A, M]]

  /**
    *
    * @return
    */
  def getName: String
}
