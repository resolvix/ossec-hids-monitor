package com.resolvix.ohm

import java.time.LocalDateTime

import com.resolvix.ohm.OssecHidsMonitor.AvailableModuleType
import com.resolvix.ohm.dao.TestOssecHidsDAO
import org.scalatest.{FlatSpec, FunSpec}

import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 10/10/16.
  */
class OssecHidsMonitorTest
  extends FunSpec
{
  val testOssecHidsDAO: dao.api.OssecHidsDAO = new TestOssecHidsDAO
  var ossecHidsMonitor: OssecHidsMonitor = _
  var alerts: List[module.api.Alert] = _
  var modules: List[AvailableModuleType] = _

  val fromDateTime: LocalDateTime = null
  val toDateTime: LocalDateTime = null

  describe("OssecHidsMonitor") {

    it("should accept a DAO to enable it to initialise itself") {
      ossecHidsMonitor = new OssecHidsMonitor(testOssecHidsDAO)
    }

    it("should obtain a list of alerts for a given period ") {
      alerts = testOssecHidsDAO.getAlertsForPeriod(
        1,
        fromDateTime,
        toDateTime
      ) match {
        case Success(listAlert: List[module.api.Alert]) =>
          listAlert

        case Failure(t: Throwable) =>
          throw t
      }
    }

    it("should obtain a list of available modules") {
      modules = OssecHidsMonitor.getAvailableModules(
        (m: AvailableModuleType) => m.getHandle.equalsIgnoreCase("sink")
      )
    }

    it("should process alerts for the relevant period using the available modules") {
      ossecHidsMonitor.process(
        alerts,
        modules,
        Map[String, Any]()
      )
    }
  }
}
