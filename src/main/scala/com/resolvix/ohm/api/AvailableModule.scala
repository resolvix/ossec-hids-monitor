package com.resolvix.ohm.api

import com.resolvix.ohm.module.api.NewStageAlert

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/02/17.
  */
trait AvailableModule {

  def doInstantiate(
    configuration: Map[String, Any]
  ): Module[_ <: Alert, _ <: ModuleAlertStatus]

  def getDescriptor: String

  def getHandle: String

  final def instantiate(
    configuration: Map[String, Any]
  ): Try[Module[_ <: Alert, _ <: ModuleAlertStatus]] = {
    try {
      Success(doInstantiate(configuration))
    } catch {
      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }
}
