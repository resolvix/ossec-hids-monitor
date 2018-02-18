package com.resolvix.ccs.api

import scala.util.Try

trait DuplexConsumerProducer[DCP <: DuplexConsumerProducer[DCP, LC, LP, RC, RP], LC, LP, RC, RP] {

  def getLeftConsumer: Consumer[LC]

  def getRightConsumer: Consumer[RC]

  def getLeftProducer: Producer[LP]

  def getRightProducer: Producer[RP]

  def registerLeft(consumer: Consumer[LC]): Try[Boolean]

  def registerRight(consumer: Consumer[RC]): Try[Boolean]

  def registerLeft(producer: Producer[LP]): Try[Boolean]

  def registerRight(producer: Producer[RP]): Try[Boolean]

}
