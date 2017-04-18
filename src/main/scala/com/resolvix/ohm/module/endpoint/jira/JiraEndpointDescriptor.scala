package com.resolvix.ohm.module.endpoint.jira

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.Result
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

object JiraEndpointDescriptor
  extends com.resolvix.ohm.module.endpoint.AbstractEndpointDescriptor[NewStageAlert, AlertStatus]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  override def newModule(
    config: Map[String, Any]
  ): Try[JiraEndpoint] = {
    Success(
      new JiraEndpoint(config)
    )
  }
}



