package com.resolvix.ohm.module

import com.resolvix.ohm.api.Alert

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/16.
  */
trait Classifiable {

  class Classification {

  }

  def classify: Try[Classification] = {
    try {
      Success(null)
    } catch {
      case t: Throwable =>
        Failure(t)
    }
  }
}
