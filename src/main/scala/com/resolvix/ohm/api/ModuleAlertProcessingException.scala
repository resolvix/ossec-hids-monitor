package com.resolvix.ohm.api

class ModuleAlertProcessingException(
  private val alert: Alert,
  private val module: Module,
  message: String,
  cause: Throwable
) extends RuntimeException(
  message,
  cause
) {

  def getAlert: Alert = alert

  def getModule: Module = module

}
