package com.resolvix.ohm.dao
import java.time.LocalDateTime

import com.resolvix.ohm.{Category, Location, Signature, SignatureCategoryMaplet}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}

import scala.util.{Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class TestOssecHidsDAO
  extends api.OssecHidsDAO
{
  override def getAlertsForPeriod(serverId: Int, fromDateTime: LocalDateTime, toDateTime: LocalDateTime): Try[List[Alert]] = ???

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

  override def getModuleAlertStatusesById(id: Int): Try[List[ModuleAlertStatus]] = ???

  override def setModuleAlertStatus(alertId: Int, moduleId: Int, reference: String, statusId: Int): Try[Boolean] = ???
}
