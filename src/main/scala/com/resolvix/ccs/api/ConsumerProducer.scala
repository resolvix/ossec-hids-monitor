package com.resolvix.ccs.api

import scala.util.Try

/**
  * The [[ConsumerProducer]] trait establishes an interface for an object
  * that is capable of consuming objects of type C, and producing objects
  * of type P.
  */
trait ConsumerProducer[CP <: ConsumerProducer[CP, C, P], C, P] {

  /**
    * Returns a [[Consumer[C]]] interface to enable the [[ConsumerProducer]]
    * client to establish a producer-consumer relationship with the instant
    * [[ConsumerProducer]] implementation.
    *
    * @return
    *   an object implementing the [[Consumer[C]]] interface
    */
  def getConsumer: Consumer[C]

  /**
    * Returns a [[Producer[P]]] interface to enable the [[ConsumerProducer]]
    * client to establish a consumer-producer relationship with the instant
    * [[ConsumerProducer]] implementation.
    *
    * @return
    *   an object implementing the [[Producer[P]]] interface
    */
  def getProducer: Producer[P]

  def register(
    consumer: Consumer[P]
  ): Try[Boolean]

  def register(
    producer: Producer[C]
  ): Try[Boolean]

  /**
    * Cross-register another object implementing the [[ConsumerProducer]]
    * interface with complementary consumption and production value
    * types such that -
    *
    * (a) object A consumes the product of object B; and
    *
    * (b) object B consumes the product of object A.
    *
    * @param consumerProducerPC
    *   the [[ConsumerProducer]] object to be cross-registered with the
    *   instant [[ConsumerProducer]]
    *
    * @tparam CPPC
    *   the type of the [[ConsumerProducer]]
    *
    * @return
    *   a value of type [[Try[Boolean]]] indicating whether cross-registration
    *   was successful and, in case of error, the respective cause.
    */
  def crossregister[CPPC <: ConsumerProducer[CPPC, P, C]](
    consumerProducerPC: CPPC
  ): Try[Boolean]
}
