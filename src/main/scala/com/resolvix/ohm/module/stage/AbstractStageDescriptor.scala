package com.resolvix.ohm.module.stage

/**
  * Created by rwbisson on 17/04/17.
  */
object AbstractStageDescriptor
{

}

/**
  * Created by rwbisson on 13/04/17.
  */
abstract class AbstractStageDescriptor[I, O, R <: StageResult[_]]
  extends com.resolvix.ohm.module.AbstractModuleDescriptor[I, O, R]
{

}