package com.resolvix.ohm.module.stage.newstage.api

import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
trait Stage[A <: Alert, R <: Result]
  extends com.resolvix.ohm.module.api.Module[A, R]
{

}
