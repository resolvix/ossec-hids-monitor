package com.resolvix.log

import org.slf4j.{Logger, LoggerFactory}

/**
  *
  */
trait Loggable {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

}
