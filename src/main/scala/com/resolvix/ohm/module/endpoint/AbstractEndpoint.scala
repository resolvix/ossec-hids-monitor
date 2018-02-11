package com.resolvix.ohm.module.endpoint

import com.resolvix.ohm.api.AlertStatus
import com.resolvix.ohm.module.api.ModuleDescriptor

import scala.util.{Success, Try}

/**
  *
  * @tparam AI
 *
  * @tparam I
 *
  * @tparam O
  */
abstract class AbstractEndpoint[AI <: AbstractEndpoint[AI, I, O, R], I, O <: AlertStatus, R <: EndpointResult[O]]
  extends com.resolvix.ohm.module.AbstractModule[AI, I, O, R]
  with api.Endpoint[I, O, R]
{
  override def close(): Try[Boolean] = {
    Success(false)
  }

  override def consume(input: I): Try[R] = ???

  override def flush(): Try[R] = ???

  override def getId: Int = ???

  override def getDescriptor: ModuleDescriptor[I, O, R] = ???

  override def initialise(): Try[Boolean] = {
    Success(false)
  }

  override def open(): Try[Boolean] = {
    Success(false)
  }

  override def terminate(): Try[Boolean] = {
    Success(false)
  }
}
