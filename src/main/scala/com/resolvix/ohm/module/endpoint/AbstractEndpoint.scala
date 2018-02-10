package com.resolvix.ohm.module.endpoint

import com.resolvix.ohm.api.AlertStatus

/**
  *
  * @tparam AI
 *
  * @tparam I
 *
  * @tparam O
  */
abstract class AbstractEndpoint[AI <: AbstractEndpoint[AI, I, O, R], I, O <: AlertStatus, R <: EndpointResult[O]]
  extends com.resolvix.ohm.module.AbstractModule[AI, I, O, R]
  with api.Endpoint[I, O, R]
{

}
