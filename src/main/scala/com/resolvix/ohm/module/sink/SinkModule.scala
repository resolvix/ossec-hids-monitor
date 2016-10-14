package com.resolvix.ohm.module.sink

import com.resolvix.ohm.{Location, Signature, api}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success, Try}

import com.resolvix.ohm.Summarizable._
import com.resolvix.ohm.Classifiable._

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkModule
  extends api.Module
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

  override def process(
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

    val x = alert.summarize
    val y = alert.classify

    val f = Promise[ModuleAlertStatus]()
    f.success(new MAS(alert.getId, getId, "refer-" + alert.getId, 0x00))
    f
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}