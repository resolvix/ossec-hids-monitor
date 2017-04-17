package com.resolvix.ohm.module.endpoint.text

import com.resolvix.ohm.module.api.{ModuleDescriptor, Result}
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

class TextEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[TextEndpoint, NewStageAlert, Result]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, Result]
{

  override def close(): Try[Boolean] = ???

  override def process[R <: Result](input: NewStageAlert): Try[R] = ???

  override def flush[R <: Result](): Try[R] = ???

  override def open(): Try[Boolean] = ???

  override def getModule: ModuleDescriptor[NewStageAlert, Result]
    = TextEndpointDescriptor

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(true)
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
