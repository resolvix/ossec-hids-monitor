package com.resolvix.ohm.module.endpoint.jira

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.{ModuleDescriptor, Result}
import com.resolvix.ohm.module.endpoint.EndpointResult
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

/**
  *
  * @param configuration
  */
class JiraEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[JiraEndpoint, NewStageAlert, AlertStatus, EndpointResult[AlertStatus]]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, AlertStatus, EndpointResult[AlertStatus]]
{
  /**
    *
    */
  override def close(): Try[Boolean] = ???

  override def process(input: NewStageAlert): Try[EndpointResult[AlertStatus]] = ???

  override def flush(): Try[EndpointResult[AlertStatus]] = ???

  /**
    * Instantiate the consumer for the module.
    *
    */
  override def open(): Try[Boolean] = ???

  override def getModule: ModuleDescriptor[NewStageAlert, AlertStatus, EndpointResult[AlertStatus]] = JiraEndpointDescriptor

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(false)
  }

  /**
    *
    * @return
    */
  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
