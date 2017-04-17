package com.resolvix.ohm.module.endpoint

/**
  *
  * @tparam AI
  * @tparam I
  * @tparam O
  */
abstract class AbstractEndpoint[AI <: AbstractEndpoint[AI, I, O], I, O]
  extends com.resolvix.ohm.module.AbstractModule[AI, I, O]
  with api.Endpoint[I, O]
{

}
