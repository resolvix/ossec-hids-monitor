package com.resolvix.ohm.module.api

trait NewStageAlert
  extends Alert
{
  def getLocation: Option[Location]

  def getSignature: Option[Signature]
}
