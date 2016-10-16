package com.resolvix.ohm.module

import com.resolvix.concurrent.Pipe
import com.resolvix.concurrent.api.RunnableConsumerProducer
import com.resolvix.ohm.{Location, Signature}
import com.resolvix.ohm.api.{Alert, ModuleAlertStatus}
import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.module.api.NewStageAlert

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/16.
  */
class NewStage(
  val ossecHidsDAO: OssecHidsDAO,
  val locationMap: Map[Int, Location],
  val signatureMap: Map[Int, Signature]
) extends RunnableConsumerProducer[Alert, NewStageAlert] {
  class AugmentedAlert(
    private val alert: Alert,
    private val location: Option[Location],
    private val signature: Option[Signature],
    private val moduleAlertStatuses: List[ModuleAlertStatus]
  ) extends NewStageAlert
    with Classifiable
    with Summarizable
  {
    override def getAlert: Alert = alert

    def getLocation: Option[Location] = location

    def getSignature: Option[Signature] = signature
  }

  override val pipe: Pipe[Alert] = new Pipe[Alert]()

  override def apply(alert: Alert): AugmentedAlert = {
    val moduleAlertStatuses: List[ModuleAlertStatus]
      = ossecHidsDAO.getModuleAlertStatusesById(
        alert.getId
      ) match {
        case Success(moduleAlertStatuses: List[ModuleAlertStatus]) =>
          moduleAlertStatuses

        case Failure(t: Throwable) =>
          List[ModuleAlertStatus]()
      }

    println("convert: ")

    new AugmentedAlert(
      alert,
      locationMap.get(alert.getLocationId),
      signatureMap.get(alert.getRuleId),
      moduleAlertStatuses
    )
  }
}
