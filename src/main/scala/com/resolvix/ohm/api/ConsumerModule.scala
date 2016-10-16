package com.resolvix.ohm.api

import com.resolvix.concurrent.api.{Consumer, ConsumerProducer, Producer}
import com.resolvix.ohm.{Category, Location, Signature}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.Try

/**
  * Created by rwbisson on 08/10/16.
  */
trait ConsumerModule
  extends ConsumerProducer[Alert, ModuleAlertStatus]
{

  def getDescriptor: String

  def getHandle: String

  def getId: Int

  def initialise(
    configuration: Map[String, Any]
  ): Try[Boolean]

  override def getConsumer: Consumer[Alert] = super.getConsumer

  override def getProducer: Producer[ModuleAlertStatus] = super.getProducer

  def terminate(): Try[Boolean]
}
