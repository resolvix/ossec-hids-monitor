package com.resolvix.ohm.module

/**
  * Provides an abstract implementation of a module instance.
  *
  * @tparam AI
  *   refers to the relevant 'AbstractInstance' -derived subclass.
  *
  * @tparam I
  *   refers to the type of alert consumed by the module
  *
  * @tparam O
  *   refers to the type of result produced by the module.
  *
  */
abstract class AbstractModule[AI <: AbstractModule[AI, I, O], I, O]
  extends com.resolvix.ohm.module.api.Module[I, O]
{

}
