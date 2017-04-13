package com.resolvix.ohm.module.endpoint.jira

import java.net.URI

object JiraSessionClosure
{
  def main(args: Array[String]): Unit = {

    println("JiraSessionClosureTest")

    val x: JiraSession = new JiraSession(
      new URI("http://192.168.0.99/jira"),
      "roger",
      "password"
    )

    x.getIssue("SEC-01")

    x.getIssue("SEC-02")

    x.close()

    System.exit(0)
  }
}
