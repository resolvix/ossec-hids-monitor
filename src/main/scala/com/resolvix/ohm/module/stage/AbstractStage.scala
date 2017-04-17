package com.resolvix.ohm.module.stage

/**
  *
  * @tparam AI
  * @tparam I
  * @tparam O
  */
abstract class AbstractStage[AI <: AbstractStage[AI, I, O], I, O]
  extends com.resolvix.ohm.module.AbstractModule[AI, I, O]
  with api.Stage[I, O]
{

}
