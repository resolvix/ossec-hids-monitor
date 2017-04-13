package com.resolvix.ohm.module.stage.api

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
    * Returns the 'Producer' of 'Alert' objects for the instance.
    *
    * @return
    */
  def getAlertProducer: Producer[A]

  /**
    * Returns the 'Consumer' of 'Result' objects for the instance.
    *
    * @return
    */
  def getResultConsumer: Consumer[R]

  /**
    * Returns the 'Producer' of 'Result' objects for the instance.
    *
    * @return
    */
  def getResultProducer: Producer[R]
}
