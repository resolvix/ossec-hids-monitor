package com.resolvix.ohm

import java.time.LocalDateTime

import com.resolvix.ohm.dao.TestOssecHidsDAO
import org.scalatest.FlatSpec

import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 10/10/16.
  */
class OssecHidMonitorTest
  extends FlatSpec
{
  val testOssecHidsDAO: dao.api.OssecHidsDAO = new TestOssecHidsDAO
  val ossecHidsMonitor: OssecHidsMonitor = new OssecHidsMonitor(testOssecHidsDAO)

  val fromDateTime: LocalDateTime = null
  val toDateTime: LocalDateTime = null

  "OssecHidsMonitor" should "accept a DAO to enable it to initialise itself" in {

  }

  it should "xxx" in {

    val alerts: List[api.Alert] = testOssecHidsDAO.getAlertsForPeriod(
      1,
      fromDateTime,
      toDateTime
    ) match {
      case Success(listAlert: List[api.Alert]) =>
        listAlert

      case Failure(t: Throwable) =>
        throw t
    }

    val modules: List[api.Module] = OssecHidsMonitor.getModules(
      (m: api.Module) => m.getHandle.equalsIgnoreCase("sink")
    )

    ossecHidsMonitor.process(
      alerts,
      modules,
      Map[String, Any]()
    )
  }

  it should "process alerts between " in {

  }

  it should "yyy" in {

  }
}