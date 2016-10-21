package com.resolvix.ohm.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.ConsumerProducer
import com.resolvix.concurrent.api.{Consumer, Producer}
import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Promise, TimeoutException}
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 08/10/16.
  */
trait Module[C <: Alert]
  extends ConsumerProducer[C, ModuleAlertStatus]
    with com.resolvix.concurrent.api.Runnable
{
  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  def run(): Unit

  def terminate(): Try[Boolean]
}
