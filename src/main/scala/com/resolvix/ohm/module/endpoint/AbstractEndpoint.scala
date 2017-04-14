package com.resolvix.ohm.module.endpoint

import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
object AbstractEndpoint
{

  /**
    *
    * @tparam AI
    * @tparam I
    * @tparam O
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, I, O], I, O]
    extends com.resolvix.ohm.module.api.Instance[I, O]
  {

  }
}

/**
  *
  * @tparam I
  * @tparam O
  */
abstract class AbstractEndpoint[I, O]
  extends AbstractModule[I, O]
{

}
