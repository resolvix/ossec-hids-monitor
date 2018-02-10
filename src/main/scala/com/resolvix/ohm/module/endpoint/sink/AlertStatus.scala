package com.resolvix.ohm.module.endpoint.sink

/**
  * Created by rwbisson on 18/04/17.
  */
class LocalAlertStatus(
  id: Int,
  moduleId: Int,
  reference: String,
  statusId: Int
) extends com.resolvix.ohm.api.AlertStatus {
  override def getId: Int = id
  override def getModuleId: Int = moduleId
  override def getReference: String = reference
  override def getStatusId: Int = statusId
}
