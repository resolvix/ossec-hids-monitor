package com.resolvix.ohm.module.stage.newstage

import java.time.Instant

import com.resolvix.log.Loggable
import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.{Alert, ModuleDescriptor}
import com.resolvix.ohm.module.stage.AbstractStage
import com.resolvix.ohm.module.stage.api.{Stage, StageResult}
import com.resolvix.ohm.module.stage.newstage.api.NewStageAlert
import com.resolvix.ohm.module.{Classifiable, Summarizable, stage}

import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.{Location, Signature}

import scala.util.{Failure, Success, Try}

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

  //val p = new Packet[V](this.getId, reader.getId, v)
  //messageWriter.write(p)

  class AugmentedAlert(
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

  def transform(alert: Alert): Try[(AugmentedAlert, StageResult[AlertStatus])] = {
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

    Success((
      new AugmentedAlert(
        alert,
        locationMap.get(alert.getLocationId),
        signatureMap.get(alert.getRuleId),
        moduleAlertStatuses
      ),
      null // TODO return an appropriate StageResult
    ))
  }

  override def consume(input: Alert): Try[Boolean] = {
    super.consume(input)
  }

  override def consume(result: StageResult[AlertStatus]): Try[Boolean] = {
    Success(true)
  }

  override def close(): Try[Boolean] = ???

  override def flush(): Try[StageResult[AlertStatus]] = ???

  override def getId: Int = ???

  override def getDescriptor: ModuleDescriptor[Alert, NewStageAlert, StageResult[AlertStatus]] = ???

  override def initialise(): Try[Boolean] = ???

  override def open(): Try[Boolean] = ???

  override def terminate(): Try[Boolean] = ???
}
