package com.resolvix.ohm.module.endpoint.api

import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
trait Endpoint[A <: Alert, R <: Result]
  extends com.resolvix.ohm.module.api.Module[A, R]
{

}
