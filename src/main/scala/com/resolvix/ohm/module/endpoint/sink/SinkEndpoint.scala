package com.resolvix.ohm.module.endpoint.sink

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Location, Signature, api}
import com.resolvix.ohm.module
import com.resolvix.ohm.module.api._
import com.resolvix.ohm.module.endpoint.AbstractEndpoint

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success, Try}

object SinkEndpoint
  extends AbstractEndpoint[Alert, Result]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[Alert, Result]
{
  override protected def getConfigurations: Array[String] = ???

  override def getDescription: String = "Module for sinking OSSEC HIDS alerts."

  override def getHandle: String = "SINK"

  protected override def newInstance(
    configuration: Map[String, Any]
  ): Try[SinkEndpoint] = {
    Success(
      new SinkEndpoint(configuration)
    )
  }
}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkEndpoint(
  configuration: Map[String, Any]
) extends AbstractEndpoint.AbstractInstance[SinkEndpoint, Alert, Result]
  with com.resolvix.ohm.module.endpoint.api.Instance[Alert, Result]
{

  override def close(): Try[Boolean] = ???

  override def consume(in: Alert, out: (Result) => Unit): Try[Boolean] = {
    /*println(
      "AID: "
        + alert.getId
        + ", RID: "
        + alert.getRuleId
        + ", LID: "
        + alert.getLocationId
        + ", L: "
        + location.get.getName
        + ", S: "
        + signature.get.getDescription
    )

    val a = new module.AugmentedAlert(alert)

    val x = a.summarize
    val y = a.classify

    val f = Promise[ModuleAlertStatus]()
    f.success(new MAS(alert.getId, getId, "refer-" + alert.getId, 0x00))
    f*/
    ???
  }

  override def flush(out: (Result) => Unit): Try[Boolean] = ???

  override def open(): Try[Boolean] = ???

  /**
    *
    * @return
    */
  override def getModule: Module[Alert, Result] = SinkEndpoint

  override def getId: Int = 2

  override def initialise(): Try[Boolean] = {
    Success(false)
  }

  class MAS(
    private val id: Int,
    private val moduleId: Int,
    private val reference: String,
    private val statusId: Int
  ) extends Result {
    override def getId: Int = id

    override def getModuleId: Int = moduleId

    override def getReference: String = reference

    override def getStatusId: Int = statusId
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
