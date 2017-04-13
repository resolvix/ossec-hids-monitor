package com.resolvix.ohm.module.stage.newstage

import java.time.Instant

import com.resolvix.ohm.module.api.{Alert, Result}
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert
import com.resolvix.ohm.module.{Classifiable, Summarizable}

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
) extends /*RunnableConsumerProducer[Alert, NewStageAlert]*/ {
  class AugmentedAlert(
    private val alert: Alert,
    private val location: Option[Location],
    private val signature: Option[Signature],
    private val moduleAlertStatuses: List[Result]
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

  //override val pipe: Pipe[Alert] = new Pipe[Alert]()

  def apply(alert: Alert): AugmentedAlert = {
    val moduleAlertStatuses: List[Result]
      = ossecHidsDAO.getModuleAlertStatusesById(
        alert.getId
      ) match {
        case Success(moduleAlertStatuses: List[Result]) =>
          moduleAlertStatuses

        case Failure(t: Throwable) =>
          List[Result]()
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
