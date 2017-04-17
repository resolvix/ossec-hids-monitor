package com.resolvix.ohm.module.endpoint.sink

import com.resolvix.ohm.module.api.{Alert, Result}

import scala.util.{Success, Try}

object SinkEndpointDescriptor
  extends com.resolvix.ohm.module.endpoint.AbstractEndpointDescriptor[Alert, Result]
    with com.resolvix.ohm.module.api.ModuleDescriptor[Alert, Result]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for sinking OSSEC HIDS alerts."

  override def getHandle: String = "SINK"

  protected override def newModule(
    configuration: Map[String, Any]
  ): Try[SinkEndpoint] = {
    Success(
      new SinkEndpoint(configuration)
    )
  }
}