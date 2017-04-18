package com.resolvix.ohm.module.endpoint.jira

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.{ModuleDescriptor, Result}
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

/**
  *
  * @param configuration
  */
class JiraEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[JiraEndpoint, NewStageAlert, AlertStatus]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, AlertStatus]
{
  /**
    *
    */
  override def close(): Try[Boolean] = ???

  override def process[R <: Result](input: NewStageAlert): Try[R] = ???

  override def flush[R <: Result](): Try[R] = ???

  /**
    * Instantiate the consumer for the module.
    *
    */
  override def open(): Try[Boolean] = ???

  override def getModule: ModuleDescriptor[NewStageAlert, AlertStatus] = JiraEndpointDescriptor

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
