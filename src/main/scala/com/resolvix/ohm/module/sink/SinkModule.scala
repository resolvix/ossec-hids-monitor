package com.resolvix.ohm.module.sink

import com.resolvix.concurrent.Pipe
import com.resolvix.ohm.{Location, Signature, api}
import com.resolvix.ohm.api.{Alert, ConsumerModule, ModuleAlertStatus}
import com.resolvix.ohm.module
import com.resolvix.ohm.module.api.NewStageAlert

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkModule
  extends ConsumerModule[NewStageAlert, ModuleAlertStatus]
{
  override def getDescriptor: String = "Module for sinking OSSEC HIDS alerts."

  override def getHandle: String = "SINK"

  override def getId: Int = 2

  override def initialise(configuration: Map[String, Any]): Try[Boolean] = {
    Success(false)
  }

  class MAS(
    private val id: Int,
    private val moduleId: Int,
    private val reference: String,
    private val statusId: Int
  ) extends api.ModuleAlertStatus {
    override def getId: Int = id

    override def getModuleId: Int = moduleId

    override def getReference: String = reference

    override def getStatusId: Int = statusId
  }

  /*override def process(
    alert: Alert,
    location: Option[Location],
    signature: Option[Signature]
  ): Promise[ModuleAlertStatus] = {
    println(
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
    f
  }*/

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
