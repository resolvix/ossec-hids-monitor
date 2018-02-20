package com.resolvix.ohm.module


import com.resolvix.ohm.module.api.Alert

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/16.
  */
trait Classifiable {

  class Classification

  def classify: Try[Classification] = {
    Try(null)
  }
}
