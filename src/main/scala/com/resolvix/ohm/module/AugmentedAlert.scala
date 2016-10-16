package com.resolvix.ohm.module

import com.resolvix.ohm.{Location, Signature, api}

/**
  * Created by rwbisson on 16/10/16.
  */
class AugmentedAlert(
  val alert: api.Alert
) extends Classifiable
  with Summarizable
{
  var location: Option[Location] = None

  var signature: Option[Signature] = None

  override protected def getAlert: api.Alert = alert
}
