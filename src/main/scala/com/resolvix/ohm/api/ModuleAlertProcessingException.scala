package com.resolvix.ohm.api

class ModuleAlertProcessingException[C <: Alert, M <: ModuleAlertStatus](
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
