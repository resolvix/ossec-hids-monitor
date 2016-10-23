package com.resolvix.ohm

import java.time._
import java.time.chrono.ChronoLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util
import java.util.NoSuchElementException

import com.resolvix.concurrent.ConsumerProducer
import com.resolvix.concurrent.api.{Consumer, Producer}
import com.resolvix.ohm.OssecHidsMonitor.ModuleType
import com.resolvix.ohm.api.{ModuleAlertProcessingException, ModuleAlertStatus, Alert => AlertT, Module => ModuleT}
import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.module.NewStage
import com.resolvix.ohm.module.api.NewStageAlert
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
    module: api.Module[AlertT]
  ) extends api.ModuleAlertStatus {
    override def getId: Int = alert.getId

    override def getModuleId: Int = module.getId

    override def getReference: String = "<none>"

    override def getStatusId: Int = 0x00
  }

  class ModuleHandle[C <: api.Alert] (

    //
    //
    //
    private val module: api.Module[AlertT],

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
    private val updateModuleAlertStatus: Function[api.ModuleAlertStatus, Try[Boolean]],

    //
    //  The function to execute to log a failure.
    //
    //  This parameter is intended to be a callback to the
    //  host application for modules to enable them to log messages
    //  without having to make reference to the mechanics of logging.
    //
    private val logFailure: Function[Throwable, Try[Boolean]]

  ) extends api.Module[AlertT]
  {

    //
    //
    //
    private val mapPromiseModuleAlertStatus: mutable.Map[Int, Promise[api.ModuleAlertStatus]]
      = new mutable.HashMap[Int, Promise[api.ModuleAlertStatus]]

    private val mapFutureModuleAlertStatus: mutable.Map[Int, Future[api.ModuleAlertStatus]]
    = new mutable.HashMap[Int, Future[api.ModuleAlertStatus]]

    def onComplete: Function[Try[api.ModuleAlertStatus], Unit] = {
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

      case Failure(e: ModuleAlertProcessingException[AlertT] @unchecked) => {
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
      alert: api.Alert,
      promiseModuleAlertStatus: Promise[api.ModuleAlertStatus]
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

    /*override def doConsume(c: C): Try[Boolean] = {
      module.doConsume(c)
    }*/

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

    /*override def process(
      alert: AlertT,
      location: Option[Location],
      signature: Option[Signature]
    ): Promise[ModuleAlertStatus] = {
      module.process(
        alert,
        location,
        signature
      )
    }*/



    //override def getConsumer: Consumer[C] = module.getConsumer

    //override def getProducer: Producer[P] = module.getProducer




    var thread: Thread = null

    override def run(): Unit = {
      thread = new Thread(module)
      thread.start()
    }

    override def finish(): Unit = {
      module.finish()
      thread.join()
    }

    override def terminate(): Try[Boolean] = {
      module.terminate()
    }
  }

  /*class ProducerP(
    alerts: List[api.Alert],
    configuration: Map[String, Any]
  ) extends Producer[api.Alert] {
    def run(): Unit = {
      alerts.foreach {
        (a: api.Alert) => produce(a)
      }
    }
  }*/

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


  type ModuleType = api.Module[AlertT]

  //
  //
  //
  private final val Modules: List[ModuleType]
    = List[ModuleType](
    (new JiraModule).asInstanceOf[ModuleType],
    (new SinkModule).asInstanceOf[ModuleType],
    (new TextModule).asInstanceOf[ModuleType]
    )

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
    for (module <- Modules) {
      println(
        module.getHandle
          + " "
          + module.getDescriptor
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

    if (commandLine.hasOption("help")) {
      displayHelp()
      return
    }

    if (commandLine.hasOption("list")) {
      displayModules()
      return
    }

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
    val modules: List[ModuleType] = List[ModuleType]()

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
    S <: ChronoLocalDateTime[_],
    T <: ChronoLocalDateTime[_],
    U <: ChronoLocalDateTime[_]
  ](
    lhs: S,
    rhs: T
  ): U ={
    if (lhs.compareTo(rhs) <= 0x00) {
      lhs.asInstanceOf[U]
    } else {
      rhs.asInstanceOf[U]
    }
  }

  def getModules: List[ModuleType] = {
    Modules
  }

  def getModules(
    filter: Function[ModuleType, Boolean]
  ): List[ModuleType] = {
    (for (module: ModuleType <- Modules if filter.apply(module))
      yield { module }).toList
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

  import OssecHidsMonitor.ModuleHandle

  type ModuleHandleType = ModuleHandle[api.Alert]

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
    modules: List[ModuleType],
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
    moduleAlertStatus: api.ModuleAlertStatus
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
    modules: List[ModuleType],
    configuration: Map[String, Any]
  ): Unit = {

    val moduleHandles: List[ModuleHandleType] = modules.map(
      (m: ModuleType) => new ModuleHandleType(
        m,
        false,
        updateModuleAlertStatus,
        logFailure
      )
    )

    moduleHandles.foreach(
      (m: ModuleHandleType) => {
        m.initialise(
          configuration.toMap
        )

        m.run()
      }
    )

    for (
      alert: api.Alert <- alerts;
      moduleHandle: ModuleHandleType <- moduleHandles
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
            if moduleAlertStatus.getModuleId == moduleHandle.getId =>
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

    moduleHandles.foreach(
      (module: ModuleHandleType) => {
        module.finish()
        module.terminate()
      }
    )
  }
}