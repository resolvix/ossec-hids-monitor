package com.resolvix.ohm.module.stage.api

/**
  * Created by rwbisson on 13/04/17.
  */
trait Stage[I, O, R <: StageResult[_]]
  extends com.resolvix.ohm.module.api.Module[I, O, R]
{

}
