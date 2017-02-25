package com.resolvix.ohm

import java.time._
import java.time.chrono.{ChronoLocalDate, ChronoLocalDateTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.lang.Thread
import java.util.NoSuchElementException

import com.resolvix.ohm.OssecHidsMonitor.AvailableModuleType
import com.resolvix.ohm.api.{AvailableModule, Consumer, ModuleAlertProcessingException, ModuleAlertStatus, Producer, Module => ModuleT}
import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.module.{NewStage, jira, sink, text}
import com.resolvix.ohm.module.api.{Module, Instance, NewStageAlert}
import com.resolvix.ohm.module.jira.JiraModule
import com.resolvix.ohm.module.sink.SinkModule
import com.resolvix.ohm.module.text.TextModule
import org.apache.commons.cli

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object OssecHidsMonitor {

  class FailureModuleAlertStatus(
    alert: api.Alert,
    module: api.Module[api.Alert, ModuleAlertStatus]
  ) extends ModuleAlertStatus {
    override def getId: Int = alert.getId

    override def getModuleId: Int = module.getId

    override def getReference: String = "<none>"

    override def getStatusId: Int = 0x00
  }

  /**
    * The ModuleHandle class is intended to wrap modules with local
    * application state and module specific functionality.
    *
    * @param instance
    * @param isEnabled
    * @param updateModuleAlertStatus
    * @param logFailure
    * @tparam C
    */
  class ActiveModule[C <: api.Alert] (

    //
    //
    //
    private val instance: Instance[_ <: Alert, _ <: ModuleAlertStatus],

    //
    //
    //
    private val isEnabled: Boolean,

    //
    //  The function to execute to perform an update
    //  to the underlying data store in respect of a module
    //  alert status.
    //
    //  This parameter is intended to be a callback to the
    //  host application for modules to enable them to persist
    //  to the data store without having to make reference
    //  to the mechanics of storage.
    //
    private val updateModuleAlertStatus: Function[ModuleAlertStatus, Try[Boolean]],

    //
    //  The function to execute to log a failure.
    //
    //  This parameter is intended to be a callback to the
    //  host application for modules to enable them to log messages
    //  without having to make reference to the mechanics of logging.
    //
    private val logFailure: Function[Throwable, Try[Boolean]]

  ) extends api.Module[Alert, ModuleAlertStatus]
  {

    //
    //
    //
    private val mapPromiseModuleAlertStatus: mutable.Map[Int, Promise[ModuleAlertStatus]]
      = new mutable.HashMap[Int, Promise[ModuleAlertStatus]]

    private val mapFutureModuleAlertStatus: mutable.Map[Int, Future[ModuleAlertStatus]]
    = new mutable.HashMap[Int, Future[ModuleAlertStatus]]

    def onComplete: Function[Try[ModuleAlertStatus], Unit] = {
      case Success(moduleAlertStatus: ModuleAlertStatus) =>
        println(
          "AID: "
            + moduleAlertStatus.getId
            + ", MID: "
            + moduleAlertStatus.getModuleId
            + ", REF: "
            + moduleAlertStatus.getReference
            + ", SID: "
            + moduleAlertStatus.getStatusId
        )

        updateModuleAlertStatus(moduleAlertStatus)

      case Failure(e: ModuleAlertProcessingException[api.Alert, api.ModuleAlertStatus] @unchecked) => {
        updateModuleAlertStatus(
          new FailureModuleAlertStatus(
            e.getAlert,
            e.getModule
          )
        )
        logFailure(e)
      }

      case Failure(t: Throwable) => {
        logFailure(t)
      }
    }

    def appendPromiseModuleAlertStatus(
      alert: Alert,
      promiseModuleAlertStatus: Promise[ModuleAlertStatus]
    ): Try[Boolean] = {
      try {
        //
        //  Associate the future module alert status with the relevant
        //  module pending receipt of a referential callback.
        //
        mapPromiseModuleAlertStatus.put(
          alert.getId,
          promiseModuleAlertStatus
        )

        //
        //  Configure the behaviour of the future module alert status upon
        //  notice from the relevant module.
        //
        promiseModuleAlertStatus.future
          .onComplete(this.onComplete)
        Success(true)
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }

    override def getDescriptor: String = {
      instance.getModule.getDescription
    }

    override def getHandle: String = {
      instance.getModule.getHandle
    }

    override def getId: Int = {
      instance.getId
    }

    override def initialise(): Try[Boolean] = {
      instance.initialise()
    }

    var thread: Thread = null

    override def run(): Unit = {
      thread = new Thread()
      thread.start()
    }

    override def finish(): Unit = {
      thread.join()
    }

    override def terminate(): Try[Boolean] = {
      instance.terminate()
    }
  }

  /**
    *
    * @tparam A
    * @tparam M
    */
  class ProducerConsumer[A <: Alert, M <: ModuleAlertStatus]
    extends com.resolvix.ccs.runnable.ProducerConsumer[ProducerConsumer[A, M], A, M]
  {
    override def doConsume(c: M): Try[Boolean] = ???

    override def doProduce(): Try[A] = ???
  }

  //
  //
  //
  private final val CommandLineOptions: cli.Options = {
    val helpOption: cli.Option = cli.Option.builder("h")
      .longOpt("help")
      .desc("Display this information")
      .build()

    val listOption: cli.Option = cli.Option.builder("l")
      .longOpt("list")
      .desc("List available modules")
      .build()

    val fromOption: cli.Option = cli.Option.builder("f")
      .longOpt("from")
      .desc("Start of the period over which OSSEC HIDS alerts "
        + "are to be processed")
      .hasArg()
      .build()

    val toOption: cli.Option = cli.Option.builder("t")
      .longOpt("to")
      .desc("End of the period over which OSSEC HIDS alerts "
        + "are to be processed")
      .hasArg()
      .build()

    (new cli.Options)
      .addOption(helpOption)
      .addOption(listOption)
      .addOption(fromOption)
      .addOption(toOption)
  }

  //
  //
  //
  private final val DefaultZoneId: ZoneId = ZoneId.systemDefault()

  //
  //
  //
  private final val DefaultZoneOffset: ZoneOffset = ZonedDateTime.now(DefaultZoneId).getOffset

  //
  //
  //
  private final val IsoDateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

  //
  //
  //
  type AvailableModuleType = Module[_ <: Alert, _ <: ModuleAlertStatus]

  //
  //
  //
  private final val AvailableModules: List[AvailableModuleType]
    = List[AvailableModuleType](
      jira.JiraModule.asInstanceOf[AvailableModuleType],
      text.TextModule.asInstanceOf[AvailableModuleType],
      sink.SinkModule.asInstanceOf[AvailableModuleType]
    )

  /**
    *
    * @param dateTime
    * @return
    */
  private def determineFromDateTime(
    dateTime: String
  ): Try[LocalDateTime] = {

    try {
      dateTime match {
        case "today" =>
          Success(
            LocalDateTime.now()
              .truncatedTo(ChronoUnit.DAYS)
          )

        case "yesterday" =>
          Success(
            LocalDateTime.now()
              .minusDays(1)
              .truncatedTo(ChronoUnit.DAYS)
          )

        case s: String =>
          Success(
            LocalDate.parse(s, IsoDateFormatter)
              .atStartOfDay()
          )

        case _ =>
          Success(
            LocalDateTime.now()
              .truncatedTo(ChronoUnit.DAYS)
          )
      }
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  private def determineToDateTime(
    dateTime: String
  ): Try[LocalDateTime] = {
    try {
      dateTime match {
        case "now" =>
          Success(LocalDateTime.now())

        case s: String =>
          Success(
            earlierOf(
              LocalDate.parse(s, IsoDateFormatter)
                .plusDays(1)
                .atStartOfDay(),
              LocalDateTime.now()
            )
          )

        case _ =>
          Success(
            LocalDateTime.now()
          )
      }
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }

  def displayHelp(): Unit = {
    val helpFormatter: cli.HelpFormatter = new cli.HelpFormatter
    helpFormatter.printHelp("ossec-hids-monitor", "", CommandLineOptions, "", true)
  }

  def displayModules(): Unit = {
    for (availableModule <- AvailableModules) {
      println(
        availableModule.getHandle
          + " "
          + availableModule.getDescription
      )
    }
  }

  def dispatch(
    args: Array[String]
  ): Unit = {
    //
    //
    //
    val commandLineParser: cli.CommandLineParser = new cli.DefaultParser()

    //
    //
    //
    val commandLine: cli.CommandLine = commandLineParser.parse(
      CommandLineOptions,
      args
    )

    commandLine.getOptions.mkString(" ") match {
      case "help" =>
        displayHelp()

      case "list" =>
        displayModules()

      case _ =>
        execute(commandLine)
    }
  }

  def execute(
    commandLine: cli.CommandLine
  ): Unit = {

    //
    //
    //
    val fromDateTime: LocalDateTime = determineFromDateTime(
      commandLine.getOptionValue("from")
    ) match {
      case Success(localDateTime: LocalDateTime) =>
        localDateTime

      case Failure(t: Throwable) =>
        throw t
    }

    //
    //
    //
    val toDateTime: LocalDateTime = determineToDateTime(
      commandLine.getOptionValue("to")
    ) match {
      case Success(localDateTime: LocalDateTime) =>
        localDateTime

      case Failure(t: Throwable) =>
        throw t
    }

    //
    //
    //
    val modules: List[AvailableModuleType] = List[AvailableModuleType]()

    val configuration: Map[String, Any] = Map[String, Any]()

    val ossecHidsDAO: OssecHidsDAO = null

    new OssecHidsMonitor(ossecHidsDAO).execute(
      modules,
      configuration,
      fromDateTime,
      toDateTime
    )
  }

  private def earlierOf[
    S <: ChronoLocalDateTime[_ <: ChronoLocalDate],
    T <: ChronoLocalDateTime[_ <: ChronoLocalDate],
    U <: ChronoLocalDateTime[_ <: ChronoLocalDate]
  ](
    lhs: S,
    rhs: T
  ): U = {
    if (lhs.compareTo(rhs) <= 0x00) {
      lhs.asInstanceOf[U]
    } else {
      rhs.asInstanceOf[U]
    }
  }

  def getAvailableModules: List[AvailableModuleType] = {
    AvailableModules
  }

  def getAvailableModules(
    filter: Function[AvailableModuleType, Boolean]
  ): List[AvailableModuleType] = {
    (
      for (module: AvailableModuleType <- AvailableModules if filter.apply(module))
        yield {
          module
        }
    ).toList
  }


  def main(
    args: Array[String]
  ): Unit = {
    dispatch(args)
  }
}

class OssecHidsMonitor(
  ossecHidsDAO: OssecHidsDAO
) {

  import OssecHidsMonitor.ActiveModule

  type ActiveModuleType = ActiveModule[Alert]

  def logFailure: PartialFunction[Throwable, Try[Boolean]] = {
    case (t: Throwable) => Success(true)
  }

  //
  //
  //
  private val locations: List[Location] = ossecHidsDAO.getLocations match {
    case Success(locations: List[Location]) =>
      locations

    case Failure(t: Throwable) =>
      List[Location]()
  }

  //
  //
  //
  private val locationMap: Map[Int, Location]
    = locations.map(
      (l: Location) => (l.getId, l)
    ).toMap

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
      try {
        val clb: ListBuffer[Category] = signatureCategoryListMap(s.getId)
        s.setCategories(clb.toList)
      } catch {
        case e: NoSuchElementException =>
          //
          //  Do nothing
          //
      }
    }

    for (c: Category <- categories) {
      try {
        val slb: ListBuffer[Signature] = categorySignatureListMap(c.getId)
        c.setSignatures(slb.toList)
      } catch {
        case e: NoSuchElementException =>
          //
          //  Do nothing
          //
      }
    }
  }

  def execute(
    modules: List[AvailableModuleType],
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

    /*process(
      alerts,
      modules,
      configuration
    )*/

    val ns: NewStage = new NewStage(ossecHidsDAO, locationMap, signatureMap)

    //val p: OssecHidsMonitor.ProducerP = new OssecHidsMonitor.ProducerP(alerts, configuration)
    //p.register(ns)
    //p.run()
  }

  private def updateModuleAlertStatus(
    moduleAlertStatus: ModuleAlertStatus
  ): Try[Boolean] = {
    println("updateModuleAlertStatus: ")
    ossecHidsDAO.setModuleAlertStatus(
      moduleAlertStatus.getId,
      moduleAlertStatus.getModuleId,
      moduleAlertStatus.getReference,
      moduleAlertStatus.getStatusId
    )
    Success(true)
  }

  def process(
    alerts: List[api.Alert],
    modules: List[AvailableModuleType],
    configuration: Map[String, Any]
  ): Unit = {

    val activeModules: List[ActiveModuleType] = modules.map(
      (am: AvailableModuleType) => new ActiveModuleType(
        am.getInstance(configuration.toMap).get,
        false,
        updateModuleAlertStatus,
        logFailure
      )
    )

    activeModules.foreach(
      (m: ActiveModuleType) => {
        m.initialise()
        m.run()
      }
    )

    for (
      alert: api.Alert <- alerts;
      activeModule: ActiveModuleType <- activeModules
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

        val moduleAlertStatus: Option[ModuleAlertStatus]
          = moduleAlertStatuses.collectFirst[ModuleAlertStatus]({
          case (moduleAlertStatus: ModuleAlertStatus)
            if moduleAlertStatus.getModuleId == activeModule.getId =>
              moduleAlertStatus
        })

        if (moduleAlertStatus.isEmpty) {

          //moduleHandle.module.(alert)

          /*moduleHandle.appendPromiseModuleAlertStatus(
            alert,
            moduleHandle.process(
              alert,
              locationMap.get(alert.getLocationId),
              signatureMap.get(alert.getRuleId)
            )
          )*/
        }
      } catch {
        case t: Throwable =>
          //
          //  Log the issue
          //
      }
    }

    println("alert processing complete")

    activeModules.foreach(
      (activeModule: ActiveModuleType) => {
        activeModule.finish()
        activeModule.terminate()
      }
    )
  }
}