package com.resolvix.ohm.module.endpoint.text

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Category, Location, Signature, api}
import com.resolvix.ohm.module.api.{Module, Result}
import com.resolvix.ohm.module.endpoint.AbstractEndpoint
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Success, Try}

object TextEndpoint
  extends AbstractEndpoint[NewStageAlert, Result]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[NewStageAlert, Result]
{

  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for rendering OSSEC HIDS alerts to a text-based report."

  override def getHandle: String = "TEXT"

  override protected def newInstance(
    configuration: Map[String, Any]
  ): Try[TextEndpoint] = {
    Success(
      new TextEndpoint(configuration)
    )
  }
}

class TextEndpoint(
  configuration: Map[String, Any]
) extends AbstractEndpoint.AbstractInstance[TextEndpoint, NewStageAlert, Result]
  with com.resolvix.ohm.module.endpoint.api.Instance[NewStageAlert, Result]
{
  /*override def doConsume(c: NewStageAlert): Try[Boolean] = ???

  override def doProduce(): Try[ModuleAlertStatus] = ???*/

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

  override def getModule: Module[NewStageAlert, Result]
    = TextEndpoint

  override def getId: Int = ???

  override def initialise(): Try[Boolean] = {
    Success(true)
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

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
