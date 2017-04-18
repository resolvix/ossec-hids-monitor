package com.resolvix.ohm.module.endpoint.api

import com.resolvix.ohm.api.AlertStatus

trait EndpointResult
  extends com.resolvix.ohm.module.api.Result
{
  def getAlertStatuses: Iterable[AlertStatus]
}
