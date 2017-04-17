package com.resolvix.ohm.api

import com.resolvix.ohm.module.api.{Alert, ResultX}

class ModuleAlertProcessingException[C <: Alert, M <: ResultX](
  private val alert: Alert,
  private val module: Module[C, M],
  message: String,
  cause: Throwable
) extends RuntimeException(
  message,
  cause
) {

  def getAlert: Alert = alert

  def getModule: Module[C, M] = module

}
