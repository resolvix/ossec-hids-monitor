package com.resolvix.ohm
import java.time.Instant

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object Alert {



}

/**
  * Created by rwbisson on 08/10/16.
  */
class Alert(

  //
  //
  //
  private val id: Int,

  //
  //
  //
  private val serverId: Int,

  //
  //
  //
  private val ruleId: Int,

  //
  //
  //
  private val level: Int,

  //
  //
  //
  private val timestamp: Instant,

  //
  //
  //
  private val locationId: Int,

  //
  //
  //
  private val sourceIp: String,

  //
  //
  //
  private val sourcePort: Int,

  //
  //
  //
  private val destinationIp: String,

  //
  //
  //
  private val destinationPort: Int,

  //
  //
  //
  private val alertId: String

) extends module.api.Alert {

  override def getId: Int = id

  override def getAlertId: String = alertId

  override def getLocationId: Int = locationId

  override def getRuleId: Int = ruleId

  override def getServerId: Int = serverId

  override def getSourceIp: String = sourceIp

  override def getSourcePort: Int = sourcePort

  override def getDestinationIp: String = destinationIp

  override def getDestinationPort: Int = destinationPort

  override def getTimestamp: Instant = timestamp

}
