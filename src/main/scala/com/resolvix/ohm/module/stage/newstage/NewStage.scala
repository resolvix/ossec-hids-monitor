package com.resolvix.ohm.module.stage.newstage

import java.time.Instant

import com.resolvix.log.Loggable
import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.Alert
import com.resolvix.ohm.module.stage.AbstractStage
import com.resolvix.ohm.module.stage.api.{Stage, StageResult}
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert
import com.resolvix.ohm.module.{Classifiable, Summarizable}

import scala.util.Try

//import com.resolvix.concurrent.api.RunnableConsumerProducer
import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.{Location, Signature}

import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 16/10/16.
  */
class NewStage(
  val ossecHidsDAO: OssecHidsDAO,
  val locationMap: Map[Int, Location],
  val signatureMap: Map[Int, Signature]
) extends AbstractStage[NewStage, Alert, NewStageAlert, StageResult[AlertStatus]]
  with Stage[Alert, NewStageAlert, StageResult[AlertStatus]]
  with Loggable
{



  /*class AugmentedAlert(
    private val alert: Alert,
    private val location: Option[Location],
    private val signature: Option[Signature],
    private val moduleAlertStatuses: List[AlertStatus]
  ) extends NewStageAlert
    with Classifiable
    with Summarizable
  {
    override def getId: Int = alert.getId

    override def getAlertId: String = alert.getAlertId

    override def getLocationId: Int = alert.getLocationId

    override def getRuleId: Int = alert.getRuleId

    override def getServerId: Int = alert.getServerId

    override def getSourceIp: String = alert.getSourceIp

    override def getSourcePort: Int = alert.getSourcePort

    override def getDestinationIp: String = alert.getDestinationIp

    override def getDestinationPort: Int = alert.getDestinationPort

    override def getTimestamp: Instant = alert.getTimestamp

    def getLocation: Option[Location] = location

    def getSignature: Option[Signature] = signature
  }

  def apply(alert: Alert): AugmentedAlert = {
    val moduleAlertStatuses: List[AlertStatus]
      = ossecHidsDAO.getModuleAlertStatusesById(
        alert.getId
      ) match {
        case Success(moduleAlertStatuses: List[AlertStatus]) =>
          moduleAlertStatuses

        case Failure(t: Throwable) =>
          List[AlertStatus]()
      }

    println("convert: ")

    new AugmentedAlert(
      alert,
      locationMap.get(alert.getLocationId),
      signatureMap.get(alert.getRuleId),
      moduleAlertStatuses
    )
  }*/
  override def consume(input: Alert): Try[StageResult[AlertStatus]] = {
    super.consume(input)
  }
}
