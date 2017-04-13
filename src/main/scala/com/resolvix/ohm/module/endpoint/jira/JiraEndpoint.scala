package com.resolvix.ohm.module.endpoint.jira


import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Instance, Module, Result}
import com.resolvix.ohm.module.endpoint.AbstractEndpoint
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.util.{Success, Try}

object JiraEndpoint
  extends AbstractEndpoint[NewStageAlert, Result]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, Result]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a JIRA-issue based report."

  override def getHandle: String = "JIRA"

  /*protected override def newInstance(
    configuration: Map[String, Any]
  ): Try[JiraModule] = {
    Success(
      new JiraModule(configuration)
    )
  }*/

  override def newInstance(
    config: Map[String, Any]
  ): Try[Instance[NewStageAlert, Result]] = {
    Success(
      new JiraEndpoint(config)
    )
  }
}

class JiraEndpoint(
  configuration: Map[String, Any]
) extends AbstractEndpoint.AbstractInstance[JiraEndpoint, NewStageAlert, Result]
  with com.resolvix.ohm.module.endpoint.api.Instance[NewStageAlert, Result]
{

  /*class ConsumerProducer
    extends com.resolvix.ccs.ConsumerProducer
    with com.resolvix.ccs.
  {
    override def doConsume(c: NewStageAlert): Try[Boolean] = ???

    override def doProduce(): Try[ModuleAlertStatus] = ???
  }*/


  /**
    * Returns the 'Consumer' of 'Alert' objects for the instance.
    *
    * @return
    */
  override def getAlertConsumer: Consumer[NewStageAlert] = ???

  /**
    * Returns the 'Producer' of 'Result' objects for the instance.
    *
    * @return
    */
  override def getResultProducer: Producer[Result] = ???

  /**
    *
    */
  override def finish(): Unit = ???

  override def getModule: Module[NewStageAlert, Result] = JiraEndpoint

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(false)
  }

  /*override def process(
    alert: Alert,
    location: Option[Location],
    signature: Option[Signature]
  ): Promise[ModuleAlertStatus] = {
    Promise()
  }*/


  /**
    *
    */
  override def run(): Unit = ???

  /**
    *
    * @return
    */
  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}

