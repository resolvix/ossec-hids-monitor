package com.resolvix.ohm.module.endpoint

import com.resolvix.ohm.api.AlertStatus

/**
  * Created by rwbisson on 17/04/17.
  */
class EndpointResult[O <: AlertStatus](
  //
  //
  //
  alertStatuses: Array[O]

  //
  //
  //
) extends com.resolvix.ohm.module.endpoint.api.EndpointResult {

  /**
    *
    * @return
    */
  def getAlertStatuses: Iterable[O] = {
    alertStatuses.toIterable
  }
}
