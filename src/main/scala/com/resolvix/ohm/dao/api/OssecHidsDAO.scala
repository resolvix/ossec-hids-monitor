package com.resolvix.ohm.dao.api

import java.time.{LocalDateTime, Period}

import com.resolvix.ohm.{Category, Location, Signature, SignatureCategoryMaplet}
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

  def getCategories: Try[List[Category]]

  def getLocations: Try[List[Location]]

  def getSignatures: Try[List[Signature]]

  def getSignatureCategoryMaplets: Try[List[SignatureCategoryMaplet]]

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