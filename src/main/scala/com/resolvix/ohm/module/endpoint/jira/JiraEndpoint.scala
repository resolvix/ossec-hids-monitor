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



  /**
    *
    */
  override def close(): Try[Boolean] = ???

  /**
    * Consume and object of type 'I', and invoke the function give by 'out'
    * as appropriate.
    *
    * @param in
    * an object representing the input to the module.
    *
    * @param out
    * the function through which one or more objects of type 'O' should be
    * directed in response.
    *
    * @return
    * a value of type 'Try[Boolean]' indicating whether the operation was
    * successful or otherwise.
    *
    */
  override def consume(in: NewStageAlert, out: (Result) => Unit): Try[Boolean] = ???

  /**
    * Flush any buffers maintained by the instance in the course of processing
    * objects of type I, and invoke the function given by 'out' as appropriate.
    *
    * @param out
    * the function through which one or more objects of type 'O' should be
    * directed.
    *
    * @return
    * a value of type 'Try[Boolean]' indicating whether the operation was
    * successful or otherwise.
    *
    */
  override def flush(out: (Result) => Unit): Try[Boolean] = ???

  /**
    * Instantiate the consumer for the module.
    *
    */
  override def open(): Try[Boolean] = ???

  override def getModule: Module[NewStageAlert, Result] = JiraEndpoint

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

