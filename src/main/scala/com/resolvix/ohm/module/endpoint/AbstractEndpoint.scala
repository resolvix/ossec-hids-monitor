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
    * @tparam A
    * @tparam R
    */
  abstract class AbstractInstance[AI <: AbstractInstance[AI, A, R], A <: Alert, R <: Result]
    extends com.resolvix.ohm.module.api.Instance[A, R]
  {

  }
}

/**
  *
  * @tparam A
  * @tparam R
  */
abstract class AbstractEndpoint[A <: Alert, R <: Result]
  extends AbstractModule[A, R]
{

}
