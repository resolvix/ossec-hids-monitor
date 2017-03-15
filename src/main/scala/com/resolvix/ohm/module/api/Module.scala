package com.resolvix.ohm.module.api

import java.util.Properties

import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}
import com.typesafe.config.Config

import scala.util.Try

/**
  *
  * @tparam A
  * @tparam M
  */
trait Module[I <: Instance[I, A, M], A <: Alert, M <: ModuleAlertStatus]
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
  def getInstance(): Try[Instance[I, A, M]]

  /**
    *
    * @param config
    * @return
    */
  def getInstance(
    config: Config
  ): Try[Instance[I, A, M]]

  /**
    *
    * @param configuration
    * @return
    */
  def getInstance(
    configuration: Map[String, Any]
  ): Try[Instance[I, A, M]]

  /**
    *
    * @param properties
    * @return
    */
  def getInstance(
    properties: Properties
  ): Try[Instance[I, A, M]]

  /**
    *
    * @return
    */
  def getName: String
}
