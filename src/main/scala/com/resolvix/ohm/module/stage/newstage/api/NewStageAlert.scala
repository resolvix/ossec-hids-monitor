package com.resolvix.ohm.module.stage.newstage.api

import com.resolvix.ohm.module.api.{Alert, Location, Signature}

trait NewStageAlert
  extends Alert
{
  def getLocation: Option[Location]

  def getSignature: Option[Signature]
}
