package com.resolvix.ohm.dao
import java.time.{Instant, LocalDateTime}

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.{Category, Location, Signature, SignatureCategoryMaplet}
import com.resolvix.ohm.module.api.Alert

import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class MockOssecHidsDAO
  extends api.OssecHidsDAO
{

  /**
    *
    * @param id
    * @param locationId
    * @param ruleId
    */
  class TestAlert(
    id: Int,
    locationId: Int,
    ruleId: Int
  ) extends Alert {
    override def getId: Int = id

    override def getAlertId: String = ???

    override def getLocationId: Int = locationId

    override def getRuleId: Int = ruleId

    override def getServerId: Int = ???

    override def getSourceIp: String = ???

    override def getSourcePort: Int = ???

    override def getDestinationIp: String = ???

    override def getDestinationPort: Int = ???

    override def getTimestamp: Instant = ???
  }

  /**
    *
    * @param alertId
    * @param moduleId
    * @param reference
    * @param statusId
    */
  class TestModuleAlertStatus(
    alertId: Int,
    moduleId: Int,
    reference: String,
    statusId: Int
  ) extends AlertStatus
  {
    override def getId: Int = alertId

    override def getModuleId: Int = moduleId

    override def getReference: String = reference

    override def getStatusId: Int = statusId
  }

  override def getAlertsForPeriod(
    serverId: Int,
    fromDateTime: LocalDateTime,
    toDateTime: LocalDateTime
  ): Try[List[Alert]] = {
    val listAlert: List[Alert] = List[Alert](
      new TestAlert(1, 1, 1),
      new TestAlert(2, 2, 2),
      new TestAlert(3, 3, 3),
      new TestAlert(4, 4, 4),
      new TestAlert(5, 5, 5)
    )
    Success(listAlert)
  }

  override def getCategories: Try[List[Category]] = {
    val listCategory: List[Category] = List[Category](
      new Category(1, "categoryA"),
      new Category(2, "categoryB"),
      new Category(3, "categoryC"),
      new Category(4, "categoryD"),
      new Category(5, "categoryE")
    )
    Success(listCategory)
  }

  override def getLocations: Try[List[Location]] = {
    val listLocation: List[Location] = List[Location](
      new Location(1, 1, "serverA"),
      new Location(2, 2, "serverB"),
      new Location(3, 3, "serverC"),
      new Location(4, 4, "serverD")
    )
    Success(listLocation)
  }

  override def getSignatures: Try[List[Signature]] = {
    val listSignature: List[Signature] = List[Signature](
      new Signature(1, 1, 1, "ruleA"),
      new Signature(2, 2, 1, "ruleB"),
      new Signature(3, 3, 1, "ruleC"),
      new Signature(4, 4, 2, "ruleD"),
      new Signature(5, 5, 3, "ruleE"),
      new Signature(6, 6, 4, "ruleF")
    )
    Success(listSignature)
  }

  override def getSignatureCategoryMaplets: Try[List[SignatureCategoryMaplet]] = {
    val listSignatureCategoryMaplet: List[SignatureCategoryMaplet] = List[SignatureCategoryMaplet](
      new SignatureCategoryMaplet(1, 4, 2),
      new SignatureCategoryMaplet(2, 5, 2),
      new SignatureCategoryMaplet(3, 6, 2)
    )
    Success(listSignatureCategoryMaplet)
  }

  override def getModuleAlertStatusesById(id: Int): Try[List[AlertStatus]] = {
    Success(List[AlertStatus]())
  }

  private val listBuffer: ListBuffer[AlertStatus] = new ListBuffer[AlertStatus]()

  override def setModuleAlertStatus(
    alertId: Int,
    moduleId: Int,
    reference: String,
    statusId: Int
  ): Try[Boolean] = {
    println("setModuleAlertStatus: ")
    listBuffer :+ new TestModuleAlertStatus(
      alertId,
      moduleId,
      reference,
      statusId
    )
    Success(true)
  }
}
