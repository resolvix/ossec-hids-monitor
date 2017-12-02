package com.resolvix.ohm.module.endpoint.text

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.{ModuleDescriptor, Result}
import com.resolvix.ohm.module.endpoint.EndpointResult
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

class TextEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[TextEndpoint, NewStageAlert, AlertStatus, EndpointResult[AlertStatus]]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, AlertStatus, EndpointResult[AlertStatus]]
{

  override def close(): Try[Boolean] = ???

  override def process(input: NewStageAlert): Try[EndpointResult[AlertStatus]] = ???

  override def flush(): Try[EndpointResult[AlertStatus]] = ???

  override def open(): Try[Boolean] = ???

  override def getModule: ModuleDescriptor[NewStageAlert, AlertStatus, EndpointResult[AlertStatus]]
    = TextEndpointDescriptor

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(true)
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
