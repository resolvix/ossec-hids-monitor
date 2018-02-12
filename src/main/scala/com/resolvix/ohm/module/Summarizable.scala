package com.resolvix.ohm.module


import com.resolvix.ohm.module.api.Alert

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

/**
  * Created by rwbisson on 16/10/16.
  */
trait Summarizable
  extends Alert
{

  object SummarizableAlert {

    abstract class SummarizableAlertFunction[S <: Alert](
      ruleId: Int
    ) extends Function[S, String] {

      def getRuleId: Int = ruleId

    }

    abstract class RegexSummarizableAlertFunction[S <: Alert](
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

    private val SummarizeDiskUsageAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^ossec\\:\\soutput\\:\\s`df\\s\\-h`\\:\\s([\\w\\/\\-\\.\\_\\[\\]\\+]+)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9\\.]+[TGMKB]?)\\s+([0-9]+\\%)\\s([\\w\\/\\-\\.\\_\\[\\]\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeLogFileRotatedAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^ossec\\:\\sFile\\srotated\\s\\(inode\\schanged\\)\\:\\s`([\\w\\/\\-\\.\\_]+)`\\.$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileAddedAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      1,
      new Regex("^([\\w\\s]+):\\s`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\n\\r.]*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileModifiedAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^([\\w\\s]+):\\s`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\n\\r.]*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizeFileRemovedAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      2,
      new Regex("^[\\w\\s]+`([\\w\\/\\-\\.\\_\\[\\]\\+]+)`[\\w\\s\\.]+$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageInstallationRequestAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sinstall\\s([\\w\\/`:\\-\\.\\~\\s\\<\\>\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageInstalledAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sstatus\\sinstalled\\s([\\w\\/`:\\-\\.\\~\\s\\<\\>\\+]+)$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val SummarizePackageRemovedAlert: SummarizableAlertFunction[Alert]
    = new RegexSummarizableAlertFunction[Alert](
      3,
      new Regex("^[0-9\\-]{10}\\s[0-9\\:]{8}\\sremove\\s([\\w\\-\\.\\:\\s\\<\\>\\+\\~]+)\\s.*$")
    ) {
      override def generateSummaryFromMatches(
        matches: Array[String]
      ): String = {
        ""
      }
    }

    private val Functions: List[SummarizableAlertFunction[Alert]]
    = List[SummarizableAlertFunction[Alert]](
      SummarizeDiskUsageAlert,
      SummarizeLogFileRotatedAlert,
      SummarizeFileAddedAlert,
      SummarizeFileModifiedAlert,
      SummarizeFileRemovedAlert,
      SummarizePackageInstallationRequestAlert,
      SummarizePackageInstalledAlert,
      SummarizePackageRemovedAlert
    )

    private val FunctionMap: Map[Int, SummarizableAlertFunction[Alert]]
    = Functions
      .map({ (f: SummarizableAlertFunction[Alert]) => (f.getRuleId, f) })
      .toMap[Int, SummarizableAlertFunction[Alert]]

    def getFunctionByRuleId[S <: Alert](
      ruleId: Int
    ): Try[SummarizableAlertFunction[S]] = {
      Try(
          FunctionMap(ruleId)
            .asInstanceOf[SummarizableAlertFunction[S]]
        )
    }
  }

  def summarize: Try[String] = {
    try {
      val f: SummarizableAlert.SummarizableAlertFunction[Alert] = SummarizableAlert.getFunctionByRuleId(getRuleId) match {
        case Success(f: SummarizableAlert.SummarizableAlertFunction[_]) =>
          f.asInstanceOf[SummarizableAlert.SummarizableAlertFunction[Alert]]

        case Failure(t: Throwable) =>
          throw t
      }
      Success(f.apply(this))
    } catch {
      case e: NoSuchElementException =>
        Failure(e)

      case NonFatal(t: Throwable) =>
        Failure(t)
    }
  }

}
