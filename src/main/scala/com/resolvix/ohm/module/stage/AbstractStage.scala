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
    * @tparam A
    * @tparam R
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, A, R], A <: Alert, R <: Result]
    extends com.resolvix.ohm.module.stage.api.Instance[A, R]
  {

  }
}

/**
  * Created by rwbisson on 13/04/17.
  */
abstract class AbstractStage[A <: Alert, R <: Result]
  extends AbstractModule[A, R]
{

}
