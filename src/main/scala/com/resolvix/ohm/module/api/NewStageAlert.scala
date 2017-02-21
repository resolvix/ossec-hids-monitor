package com.resolvix.ohm.module.api

import com.resolvix.ohm.api.Alert
import com.resolvix.ohm.{Location, Signature}

trait NewStageAlert
  extends Alert
{
  def getLocation: Option[Location]

  def getSignature: Option[Signature]
}
