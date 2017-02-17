package com.resolvix.ohm.module.sink

import com.resolvix.ohm.OssecHidsMonitor.ActiveModule
import com.resolvix.ohm.{Location, Signature, api}
import com.resolvix.ohm.api.{Alert, Module, ModuleAlertStatus, AvailableModule}
import com.resolvix.ohm.module
import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.NewStageAlert

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success, Try}

object SinkModule
  extends AvailableModule
{
  def doInstantiate(
    configuration: Map[String, Any]
  ): Module[_ <: Alert, _ <: ModuleAlertStatus] = {
    new SinkModule(configuration)
  }

  override def getDescriptor: String = "Module for sinking OSSEC HIDS alerts."

  override def getHandle: String = "SINK"
}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkModule(
  configuration: Map[String, Any]
) extends AbstractModule[SinkModule, Alert, ModuleAlertStatus] {
  override def doConsume(c: Alert): Try[Boolean] = {
    println("SinkModule.doConsume: " + c.toString)
    Success(true)
  }

  def getDescriptor: String = SinkModule.getDescriptor

  def getHandle: String = SinkModule.getHandle

  override def getId: Int = 2

  override def initialise(): Try[Boolean] = {
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


  override def doProduce(): Try[ModuleAlertStatus] = ???

  override def run(): Unit = {
    println("SinkModule.run: starting thread")
    //super.run()
    println("SinkModule.run: thread finished")
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
