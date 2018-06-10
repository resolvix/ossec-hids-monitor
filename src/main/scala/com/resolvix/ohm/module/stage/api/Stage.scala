package com.resolvix.ohm.module.stage.api

import scala.collection.mutable.MutableList
import scala.util.Try

import com.resolvix.ohm.module.api.Module;

/**
  * Created by rwbisson on 13/04/17.
  */
trait Stage[I, O, R <: StageResult[_]]
  extends Module[I, O, R] {

}