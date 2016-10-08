package com.resolvix.ohm.dao.api

import java.time.{LocalDateTime, Period}

import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}

import scala.util.Try

/**
  * Created by rwbisson on 08/10/16.
  */
trait OssecHidsDAO {

  def getAlertsForPeriod(
    serverId: Int,
    fromDateTime: LocalDateTime,
    toDateTime: LocalDateTime
  ): Try[List[Alert]]

  def getModuleAlertStatusesById(
    id: Int
  ): Try[List[ModuleAlertStatus]]

  def setModuleAlertStatus(
    alertId: Int,
    moduleId: Int,
    reference: String,
    statusId: Int
  ): Try[Boolean]

}
