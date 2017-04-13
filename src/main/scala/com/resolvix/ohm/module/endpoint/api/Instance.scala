package com.resolvix.ohm.module.endpoint.api

import com.resolvix.ccs.runnable.api.{Consumer, Producer}
import com.resolvix.ohm.module.api.{Alert, Result}

/**
  * Created by rwbisson on 13/04/17.
  */
trait Instance[A <: Alert, R <: Result]
  extends com.resolvix.ohm.module.api.Instance[A, R]
{
  /**
    * Returns the 'Consumer' of 'Alert' objects for the instance.
    *
    * @return
    */
  def getAlertConsumer: Consumer[A]

  /**
    * Returns the 'Producer' of 'Result' objects for the instance.
    *
    * @return
    */
  def getResultProducer: Producer[R]
}
