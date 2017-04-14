package com.resolvix.ohm.module.endpoint.api

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
trait Instance[A <: Alert, R <: Result]
  extends com.resolvix.ohm.module.api.Instance[A, R]
{

}
