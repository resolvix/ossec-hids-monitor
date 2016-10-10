package com.resolvix.ohm

import java.time._
import java.time.chrono.ChronoLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util
import java.util.NoSuchElementException

import com.resolvix.ohm.api.{ModuleAlertStatus, Alert => AlertT, Module => ModuleT}
import com.resolvix.ohm.dao.api.OssecHidsDAO
import com.resolvix.ohm.module.jira.JiraModule
import com.resolvix.ohm.module.sink.SinkModule
import com.resolvix.ohm.module.text.TextModule
import org.apache.commons.cli

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
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
      alert: AlertT,
      location: Option[Location],
      signature: Option[Signature]
    )(
      implicit ec: ExecutionContext
    ): Try[Future[ModuleAlertStatus]] = {
      module.process(
        alert,
        location,
        signature
      )
    }

    override def terminate(): Try[Boolean] = {
      module.terminate()
    }
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

  def onFailure: PartialFunction[Throwable, Unit] = {
    case (t: Throwable) =>

  }

  def onSuccess: PartialFunction[ModuleAlertStatus, Unit] = {
    case (moduleAlertStatus: ModuleAlertStatus) =>
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
  }

  def update: PartialFunction[ModuleAlertStatus, Try[Boolean]] = {
    case (moduleAlertStatus: ModuleAlertStatus) => Success(true)
  }

  def logFailure: PartialFunction[Throwable, Try[Boolean]] = {
    case (t: Throwable) => Success(true)
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
  private final val Modules: List[api.Module] = {

    val jiraModule: JiraModule = new JiraModule
    val sinkModule: SinkModule = new SinkModule
    val textModule: TextModule = new TextModule

    List[api.Module](
      new ModuleHandle(
        jiraModule,
        false,
        onSuccess,
        onFailure,
        update,
        logFailure
      ),
      new ModuleHandle(
        sinkModule,
        false,
        onSuccess,
        onFailure,
        update,
        logFailure
      ),
      new ModuleHandle(
        textModule,
        false,
        onSuccess,
        onFailure,
        update,
        logFailure
      )
    )
  }


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
    for (module: api.Module <- Modules) {
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


    val x: String = commandLine.getOptionValue("f")

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
    val modules: List[api.Module] = List[api.Module]()

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

  def getModules: List[api.Module] = {
    Modules
  }

  def getModules(
    filter: Function[api.Module, Boolean]
  ): List[api.Module] = {
    (for (module: api.Module <- Modules if filter.apply(module))
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
    modules: List[api.Module],
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
    modules: List[api.Module],
    configuration: Map[String, Any]
  ): Unit = {

    for (module: api.Module <- modules) {
      module.initialise(
        configuration.toMap
      )
    }

    for (
      alert: api.Alert <- alerts;
      module: api.Module <- modules
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
            if moduleAlertStatus.getModuleId == module.getId =>
              moduleAlertStatus
        })

        if (moduleAlertStatus.isEmpty) {

          val location: Option[Location] = locationMap.get(alert.getLocationId)
          val signature: Option[Signature] = signatureMap.get(alert.getRuleId)

          module match {
            case moduleHandle: ModuleHandle =>
              moduleHandle.process(
                alert,
                location,
                signature
              ) match {
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