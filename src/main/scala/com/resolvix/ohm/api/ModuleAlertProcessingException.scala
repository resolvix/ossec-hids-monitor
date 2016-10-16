package com.resolvix.ohm.api

class ModuleAlertProcessingException[C <: Alert, P <: ModuleAlertStatus](
  private val alert: Alert,
  private val module: ConsumerModule[C, P],
  message: String,
  cause: Throwable
) extends RuntimeException(
  message,
  cause
) {

  def getAlert: Alert = alert

  def getModule: ConsumerModule[C, P] = module

}
