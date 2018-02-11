package com.resolvix.ohm.module.endpoint.sink

import com.resolvix.log.Loggable
import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.{Alert, ModuleDescriptor, Result}
import com.resolvix.ohm.module.endpoint.EndpointResult

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 10/10/16.
  */
class SinkEndpoint(
  configuration: Map[String, Any]
) extends com.resolvix.ohm.module.endpoint.AbstractEndpoint[SinkEndpoint, Alert, AlertStatus, EndpointResult[AlertStatus]]
  with com.resolvix.ohm.module.endpoint.api.Endpoint[Alert, AlertStatus, EndpointResult[AlertStatus]]
  with Loggable
{

  override def close(): Try[Boolean] = {
    Success(false)
  }

  override def consume(input: Alert): Try[EndpointResult[AlertStatus]] = {
    log.debug(
      "AID: "
        + input.getId
        + ", RID: "
        + input.getRuleId
        + ", LID: "
        + input.getLocationId
    )

    /*val a = new module.AugmentedAlert(input)

    val x = a.summarize
    val y = a.classify*/

    /*val f = Promise[ModuleAlertStatus]()
    f.success(new MAS(alert.getId, getId, "refer-" + alert.getId, 0x00))
    f*/
    Success(
      new EndpointResult(
        Array[AlertStatus](
          new LocalAlertStatus(
            input.getId,
            0x00,
            "SINK",
            0x00
          )
        )
      ).asInstanceOf[EndpointResult[AlertStatus]]
    )
  }

  override def flush(): Try[EndpointResult[AlertStatus]] = {
    Success(new EndpointResult(Array[AlertStatus]()).asInstanceOf[EndpointResult[AlertStatus]])
  }

  override def open(): Try[Boolean] = {
    Success(false)
  }

  /**
    *
    * @return
    */
  override def getDescriptor: ModuleDescriptor[Alert, AlertStatus, EndpointResult[AlertStatus]]
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
  ) extends AlertStatus {
    override def getId: Int = id

    override def getModuleId: Int = moduleId

    override def getReference: String = reference

    override def getStatusId: Int = statusId
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
