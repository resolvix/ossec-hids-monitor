package com.resolvix.ohm.api

class ModuleAlertProcessingException[C <: Alert](
  private val alert: Alert,
  private val module: Module[C],
  message: String,
  cause: Throwable
) extends RuntimeException(
  message,
  cause
) {

  def getAlert: Alert = alert

  def getModule: Module[C] = module

}
