package com.resolvix.ohm.module.endpoint

import com.resolvix.ohm.api.AlertStatus

/**
  * Created by rwbisson on 13/04/17.
  */
object AbstractEndpointDescriptor
{

}

/**
  *
  * @tparam I
  * @tparam O
  */
abstract class AbstractEndpointDescriptor[I, O <: AlertStatus, R <: EndpointResult[O]]
  extends com.resolvix.ohm.module.AbstractModuleDescriptor[I, O, R]
{

}