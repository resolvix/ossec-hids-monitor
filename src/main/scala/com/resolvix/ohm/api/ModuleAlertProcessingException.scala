package com.resolvix.ohm.api

class ModuleAlertProcessingException(
  private val alert: Alert,
  private val module: ConsumerModule,
  message: String,
  cause: Throwable
) extends RuntimeException(
  message,
  cause
) {

  def getAlert: Alert = alert

  def getModule: ConsumerModule = module

}
