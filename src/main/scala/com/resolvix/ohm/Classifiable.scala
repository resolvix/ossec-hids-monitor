package com.resolvix.ohm

import com.resolvix.ohm.Classifiable.Classification

import scala.util.{Failure, Success, Try}

/**
  *
  */
object Classifiable {

  class Classification {

  }

  abstract class ClassifiableAny {
    def classify: Try[Classification]
  }

  implicit class ClassifiableAlert(
    alert: api.Alert
  ) extends ClassifiableAny {

    def classify: Try[Classification] = {
      try {
        Success(null)
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }
  }
}
