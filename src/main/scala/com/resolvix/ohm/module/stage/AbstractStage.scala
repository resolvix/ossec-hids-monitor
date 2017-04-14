package com.resolvix.ohm.stage

import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
object AbstractStage
{

  /**
    *
    * @tparam AI
    * @tparam I
    * @tparam O
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, I, O], I, O]
    extends com.resolvix.ohm.module.stage.api.Instance[I, O]
  {

  }
}

/**
  * Created by rwbisson on 13/04/17.
  */
abstract class AbstractStage[I, O]
  extends AbstractModule[I, O]
{

}
