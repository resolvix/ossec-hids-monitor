package com.resolvix.ohm.module.endpoint.sink

import com.resolvix.ohm.module.api.{Alert, ModuleDescriptor, Result, ResultX}

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[SinkEndpoint, Alert, Result]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[Alert, Result] {

  override def close(): Try[Boolean] = ???

  override def process[R <: Result](input: Alert): Try[R] = {
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

  override def flush[R <: Result](): Try[R] = ???

  override def open(): Try[Boolean] = ???

  /**
    *
    * @return
    */
  override def getModule: ModuleDescriptor[Alert, Result]
    = SinkEndpointDescriptor

  override def getId: Int = 2

  override def initialise(): Try[Boolean] = {
    Success(false)
  }

  class MAS(
    private val id: Int,
    private val moduleId: Int,
    private val reference: String,
    private val statusId: Int
  ) extends ResultX {
    override def getId: Int = id

    override def getModuleId: Int = moduleId

    override def getReference: String = reference

    override def getStatusId: Int = statusId
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
