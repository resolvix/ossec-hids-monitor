package com.resolvix.ohm.module.stage

/**
  *
  * @tparam AI
  * @tparam I
  * @tparam O
  */
abstract class AbstractStage[AI <: AbstractStage[AI, I, O, R], I, O, R <: StageResult[_]]
  extends com.resolvix.ohm.module.AbstractModule[AI, I, O, R]
  with api.Stage[I, O, R]
{

}
