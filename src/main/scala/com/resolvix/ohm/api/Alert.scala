package com.resolvix.ohm.api

import java.time.Instant

trait Alert {

  def getId: Int

  def getAlertId: String

  def getLocationId: Int

  def getRuleId: Int

  def getServerId: Int

  def getSourceIp: String

  def getSourcePort: Int

  def getDestinationIp: String

  def getDestinationPort: Int

  def getTimestamp: Instant

}
