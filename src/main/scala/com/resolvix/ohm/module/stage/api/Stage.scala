package com.resolvix.ohm.module.stage.api

import com.resolvix.ccs.api.Consumer

import scala.collection.mutable.MutableList
import scala.util.Try

/**
  * Created by rwbisson on 13/04/17.
  */
trait Stage[I, O, R <: StageResult[_]]
  extends com.resolvix.ohm.module.api.Module[I, O, R] {
  /**
    *
    *
    * @param output
    *   an object of type [[O]]
    *
    * @return
    *   a [[Try]] object indicating whether the product operation was
    *   successful
    */
  protected def produce(output: O): Try[Boolean]
}
