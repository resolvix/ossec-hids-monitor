package com.resolvix.concurrentx.api

import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.sio.api

import scala.concurrent.duration._
import scala.util.Try

/**
  *
  * @tparam C
  *   refers to the type of the consumer
  *
  * @tparam P
  *   refers to the type of the producer
  *
  * @tparam T
  *   refers to the type of transport for the transmission of values of
  *   type V from the producer to the consumer for supply to the producer
  *   upon receipt of a call to the open method
  *
  * @tparam V
  *   refers to the type of values to be received by the consumer
  *   from the producer
  *
  */
trait Consumer[V]
  extends Actor[Consumer[V], Producer[V], V]
{

  /**
    *
    * @param configuration
    * @return
    */
  override def initialise(
    configuration: Configuration
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  def close(
    producer: Producer[V]
  ): Try[Boolean]

  /**
    *
    * @return
    */
  def open: Try[Reader[V]]

  /**
    * Obtains an object of type Writer that may be used for the transmission
    * of values of type V to the instant Consumer by a producer of type P.
    *
    * @param producer
    *   the producer for which the Writer object is to be obtained.
    *
    * @return
    *   an object of type Writer configured correctly to enable the producer,
    *   to write values to the instant Consumer.
    */
  @throws[ProducerNotRegisteredException]
  def open[W <: Writer[V]](
    producer: Producer[V]
  ): Try[W]

  /**
    *
    * @param producer
    * @return
    */
  override def register(
    producer: Producer[V]
  ): Try[Boolean]

  /**
    *
    * @param producer
    * @return
    */
  override def unregister(
    producer: Producer[V]
  ): Try[Boolean]
}