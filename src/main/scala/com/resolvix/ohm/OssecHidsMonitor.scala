package com.resolvix.ohm

import java.time.{LocalDateTime, Period}
import java.util

import com.resolvix.ohm.api.{ModuleAlertStatus, Alert => AlertT, Module => ModuleT}
import com.resolvix.ohm.dao.api.OssecHidsDAO

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object OssecHidsMonitor {

  class ModuleHandle (

    //
    //
    //
    private val module: api.Module,

    //
    //
    //
    private val isEnabled: Boolean,

    //
    //  The partial function to execute, in relation to the
    //  module alert status, upon success of the Future event.
    //
    private val onSuccess: PartialFunction[ModuleAlertStatus, Unit],

    //
    //  The partial function to execute, in relation to the
    //  module alert status, upon failure of the Future event.
    //
    private val onFailure: PartialFunction[Throwable, Unit],

    //
    //  The partial function to execute to perform an update
    //  to the underlying data store in respect of a module
    //  alert status.
    //
    private val update: PartialFunction[ModuleAlertStatus, Try[Boolean]],

    //
    //
    //
    private val logFailure: PartialFunction[Throwable, Try[Boolean]]

  ) extends api.Module {

    //
    //
    //
    private val mapFutureModuleAlertStatus: mutable.Map[Int, Future[api.ModuleAlertStatus]]
      = new mutable.HashMap[Int, Future[api.ModuleAlertStatus]]

    //
    //
    //
    private val onFutureSuccess: PartialFunction[ModuleAlertStatus, Unit]
      = new PartialFunction[ModuleAlertStatus, Unit] {
        override def apply(m: ModuleAlertStatus): Unit = {
          update(m)
        }

        override def isDefinedAt(m: ModuleAlertStatus): Boolean = {
          m match {
            case mm: ModuleAlertStatus =>
              true

            case _ =>
              false
          }
        }
      }

    private val onFutureFailure: PartialFunction[Throwable, Unit]
      = new PartialFunction[Throwable, Unit] {
      override def apply(t: Throwable): Unit = {
        logFailure(t)
      }

      override def isDefinedAt(t: Throwable): Boolean = {
        t match {
          case tt: Throwable =>
            true

          case _ =>
            false
        }
      }
    }

    def appendFutureModuleAlertStatus(
      alert: api.Alert,
      futureModuleAlertStatus: Future[api.ModuleAlertStatus]
    ): Try[Boolean] = {
      try {
        //
        //  Configure the behaviour of the future module alert status upon
        //  notice from the relevant module.
        //
        futureModuleAlertStatus.onSuccess[Unit](onFutureSuccess)
        futureModuleAlertStatus.onFailure[Unit](onFutureFailure)

        //
        //  Associate the future module alert status with the relevant
        //  module pending receipt of a referential callback.
        //
        mapFutureModuleAlertStatus.put(
          alert.getId,
          futureModuleAlertStatus
        )

        Success(true)
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }

    def updateModuleAlertStatus(
      alert: api.Alert,
      reference: String,
      statusId: Int
    ): Try[Boolean] = {
      /*
      )*/
      Success(false)
    }

    override def getDescriptor: String = {
      module.getDescriptor
    }

    override def getHandle: String = {
      module.getHandle
    }

    override def getId: Int = {
      module.getId
    }

    override def initialise(
      configuration: Map[String, Any]
    ): Try[Boolean] = {
      module.initialise(configuration)
    }

    override def process(
      alert: api.Alert
    ): Try[Future[ModuleAlertStatus]] = {
      module.process(alert)
    }

    override def terminate(): Try[Boolean] = {
      module.terminate()
    }
  }

  private def determineFromDateTime(
    dateTime: String
  ): LocalDateTime = {
    LocalDateTime.now()
  }

  private def determineToDateTime(
    dateTime: String
  ): LocalDateTime = {
    LocalDateTime.now()
  }

  def dispatch(): Unit = {
    //
    //
    //
    val fromDateTime: LocalDateTime = determineFromDateTime("")

    //
    //
    //
    val toDateTime: LocalDateTime = determineToDateTime("")

    //
    //
    //
    val modules: List[ModuleHandle] = List[ModuleHandle]()

    val configuration: Map[String, Any] = Map[String, Any]()

    val ossecHidsDAO: OssecHidsDAO = null

    (new OssecHidsMonitor(ossecHidsDAO)).execute(
      modules,
      configuration,
      fromDateTime,
      toDateTime
    )
  }

  def main(
    args: Array[String]
  ): Unit = {

  }
}

class OssecHidsMonitor(
  ossecHidsDAO: OssecHidsDAO
) {

  import OssecHidsMonitor.ModuleHandle

  //
  //
  //
  private val categories: List[Category] = ossecHidsDAO.getCategories match {
    case Success(categories: List[Category]) =>
      categories

    case Failure(t: Throwable) =>
      List[Category]()
  }

  //
  //
  //
  private val categoryMap: Map[Int, Category]
    = categories.map(
      (c: Category) => (c.getId, c)
    ).toMap

  //
  //
  //
  private val signatures: List[Signature] = ossecHidsDAO.getSignatures match {
    case Success(signatures: List[Signature]) =>
      signatures

    case Failure(t: Throwable) =>
      List[Signature]()
  }

  //
  //
  //
  private val signatureMap: Map[Int, Signature]
    = signatures.map(
      (s: Signature) => (s.getRuleId, s)
    ).toMap

  //
  //
  //

  crossReferenceSignaturesAndCategories(
    signatureMap,
    categoryMap
  )

  private val updateModuleAlertStatus: PartialFunction[(api.Alert, api.Module, String, Int), Try[Boolean]]
    = new PartialFunction[(api.Alert, api.Module, String, Int), Try[Boolean]] {
      override def apply(p: (api.Alert, api.Module, String, Int)): Try[Boolean] = {
        ossecHidsDAO.setModuleAlertStatus(
          p._1.getId,
          p._2.getId,
          p._3,
          p._4
        )
      }

      override def isDefinedAt(p: (api.Alert, api.Module, String, Int)): Boolean = {
        p match {
          case pp: (api.Alert, api.Module, String, Int) =>
            true

          case _ =>
            false
        }
      }
    }

  def crossReferenceSignaturesAndCategories(
    signatureMap: Map[Int, Signature],
    categoryMap: Map[Int, Category]
  ): Unit = {
    val signatureCategoryMaplets: List[SignatureCategoryMaplet]
      = ossecHidsDAO.getSignatureCategoryMaplets match {
        case Success(signatureCategoryMaplets: List[SignatureCategoryMaplet])   =>
          signatureCategoryMaplets

        case Failure(t: Throwable) =>
          List[SignatureCategoryMaplet]()
      }

    val signatureCategoryListMap: mutable.Map[Int, ListBuffer[Category]]
      = mutable.Map[Int, ListBuffer[Category]]()

    val categorySignatureListMap: mutable.Map[Int, ListBuffer[Signature]]
      = mutable.Map[Int, ListBuffer[Signature]]()

    for (scm: SignatureCategoryMaplet <- signatureCategoryMaplets) {
      for (
        s: Signature <- signatureMap.get(scm.getRuleId);
        c: Category <- categoryMap.get(scm.getCategoryId)
      ) {
        val slb: ListBuffer[Signature] = categorySignatureListMap
          .getOrElseUpdate(scm.getCategoryId, ListBuffer[Signature]())

        val clb: ListBuffer[Category] = signatureCategoryListMap
          .getOrElseUpdate(scm.getRuleId, ListBuffer[Category]())

        slb :+ s
        clb :+ c
      }
    }

    for (s: Signature <- signatures) {
      val clb: ListBuffer[Category] = signatureCategoryListMap(s.getId)
      s.setCategories(clb.toList)
    }

    for (c: Category <- categories) {
      val slb: ListBuffer[Signature] = categorySignatureListMap(c.getId)
      c.setSignatures(slb.toList)
    }
  }

  def execute(
    modules: List[ModuleHandle],
    configuration: Map[String, Any],
    fromDateTime: LocalDateTime,
    toDateTime: LocalDateTime
  ): Unit = {
    //
    //
    //
    val alerts: List[Alert] = ossecHidsDAO.getAlertsForPeriod(
      0x01,
      fromDateTime,
      toDateTime
    ) match {
      case Success(alerts: List[_]) =>
        alerts.asInstanceOf[List[Alert]]

      case Failure(t: Throwable) =>
        List[Alert]()
    }

    process(
      alerts,
      modules,
      configuration
    )
  }

  def process(
    alerts: List[api.Alert],
    modules: List[ModuleHandle],
    configuration: Map[String, Any]
  ): Unit = {

    for (module: api.Module <- modules) {
      module.initialise(
        configuration.toMap
      )
    }

    for (
      alert: api.Alert <- alerts;
      moduleHandle: ModuleHandle <- modules
    ) {

      try {
        val moduleAlertStatuses: List[ModuleAlertStatus]
          = ossecHidsDAO.getModuleAlertStatusesById(
            alert.getId
          ) match {
            case Success(moduleAlertStatuses: List[ModuleAlertStatus]) =>
              moduleAlertStatuses

            case Failure(t: Throwable) =>
              List[ModuleAlertStatus]()
          }

        //
        //  if alertStatuses contains a member with a moduleId that
        //  is equal to the current moduleId, the process has been
        //  executed against that event and should not be executed
        //  again
        //
        if (true) {
          moduleHandle.process(alert) match {
            case Success(futureModuleAlertStatus: Future[api.ModuleAlertStatus]) =>
              moduleHandle.appendFutureModuleAlertStatus(
                alert,
                futureModuleAlertStatus
              )

            case Failure(t: Throwable) =>
              updateModuleAlertStatus(
                alert,
                moduleHandle,
                "",
                0x00
              )
          }
        }
      } catch {
        case t: Throwable =>
          //
          //  Log the issue
          //
      }
    }

    for (module: api.Module <- modules) {
      module.terminate()
    }

  }
}