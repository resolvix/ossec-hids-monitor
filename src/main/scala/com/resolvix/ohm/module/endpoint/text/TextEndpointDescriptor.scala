package com.resolvix.ohm.module.endpoint.text

import com.resolvix.ohm.module.api.Result
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

object TextEndpointDescriptor
  extends com.resolvix.ohm.module.endpoint.AbstractEndpointDescriptor[NewStageAlert, Result]
    with com.resolvix.ohm.module.api.ModuleDescriptor[NewStageAlert, Result]
{

  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a text-based report."

  override def getHandle: String = "TEXT"

  override protected def newModule(
    configuration: Map[String, Any]
  ): Try[TextEndpoint] = {
    Success(
      new TextEndpoint(configuration)
    )
  }
}
