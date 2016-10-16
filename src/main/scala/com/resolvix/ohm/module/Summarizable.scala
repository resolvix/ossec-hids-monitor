package com.resolvix.ohm.module

import com.resolvix.ohm.api

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

/**
  * Created by rwbisson on 16/10/16.
  */
trait Summarizable {

  object SummarizableAlert {

    abstract class SummarizableAlertFunction[S <: api.Alert](
      ruleId: Int
    ) extends Function[S, String] {

      def getRuleId: Int = ruleId

    }

    abstract class RegexSummarizableAlertFunction[S <: api.Alert](
      ruleId: Int,
      regex: Regex
    ) extends SummarizableAlertFunction[S](
      ruleId
    ) {
      def generateSummaryFromMatches(
        matches: Array[String]
      ): String

      def apply(s: S): String = {
        ""
      }
    }

    private val SummarizeDiskUsageAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^ossec\\:\\soutput\\:\\s`df\\s\\-h`\\:\\s([\\w\\/\\-\\.\\_\\[\\]\\+]+)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9]+\\%)\\s([\\w\\/\\-\\.\\_\\[\\]\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeLogFileRotatedAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^ossec\\:\\sFile\\srotated\\s\\(inode\\schanged\\)\\:\\s`([\\w\\/\\-\\.\\_]+)`\\.$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileAddedAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      1,
      new Regex("^([\\w\\s]+):\\s`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\n\\r.]*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileModifiedAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^([\\w\\s]+):\\s`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\n\\r.]*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileRemovedAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      2,
      new Regex("^[\\w\\s]+`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\w\\s\\.]+$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageInstallationRequestAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sinstall\\s([\\w\\/`:\\-\\.\\~\\s\\<\\>\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageInstalledAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sstatus\\sinstalled\\s([\\w\\/`:\\-\\.\\~\\s\\<\\>\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageRemovedAlert: SummarizableAlertFunction[api.Alert]
    = new RegexSummarizableAlertFunction[api.Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sremove\\s([\\w\\-\\.\\:\\s\\<\\>\\+\\~]+)\\s.*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val Functions: List[SummarizableAlertFunction[api.Alert]]
    = List[SummarizableAlertFunction[api.Alert]](
      SummarizeDiskUsageAlert,
      SummarizeLogFileRotatedAlert,
      SummarizeFileAddedAlert,
      SummarizeFileModifiedAlert,
      SummarizeFileRemovedAlert,
      SummarizePackageInstallationRequestAlert,
      SummarizePackageInstalledAlert,
      SummarizePackageRemovedAlert
    )

    private val FunctionMap: Map[Int, SummarizableAlertFunction[api.Alert]]
    = Functions
      .map({ (f: SummarizableAlertFunction[api.Alert]) => (f.getRuleId, f) })
      .toMap[Int, SummarizableAlertFunction[api.Alert]]

    def getFunctionByRuleId[S <: api.Alert](
      ruleId: Int
    ): Try[SummarizableAlertFunction[S]] = {
      try {
        Success(
          FunctionMap(ruleId)
            .asInstanceOf[SummarizableAlertFunction[S]]
        )
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }
  }

  protected def getAlert: api.Alert

  def summarize: Try[String] = {
    try {
      val f: SummarizableAlert.SummarizableAlertFunction[api.Alert] = SummarizableAlert.getFunctionByRuleId(getAlert.getRuleId) match {
        case Success(f: SummarizableAlert.SummarizableAlertFunction[_]) =>
          f.asInstanceOf[SummarizableAlert.SummarizableAlertFunction[api.Alert]]

        case Failure(t: Throwable) =>
          throw t
      }
      Success(f.apply(getAlert))
    } catch {
      case e: NoSuchElementException =>
        Failure(e)

      case t: Throwable =>
        Failure(t)
    }
  }

}
