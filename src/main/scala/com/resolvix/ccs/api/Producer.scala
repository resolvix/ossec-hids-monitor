package com.resolvix.ccs.api

import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.sio.api

import scala.util.Try

/**
  *
  * @tparam P
  *   refers to the type of the producer
  *
  * @tparam C
  *   refers to the type of the consumer
  *
  * @tparam T
  *   refers to the type of transport for the transmission of values of
  *   type V from the producer to the consumer for supply to the consumer
  *   upon receipt of a call to the open method
  *
  * @tparam V
  *   refers to the type of values to be transmitted by the producer
  *   to the consumer
  *
  */
trait Producer[V]
  extends Actor[Producer[V], Consumer[V]]
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
    * @param consumer
    * @return
    */
  def close(
    consumer: Consumer[V]
  ): Try[Boolean]

  /**
    *
    * @return
    */
  def open: Try[Writer[V]]

  /**
    *
    * @param consumer
    * @return
    */
  @throws[ConsumerNotRegisteredException]
  def open(
    consumer: Consumer[V]
  ): Try[Reader[V]]

  /**
    *
    * @param consumer
    * @return
    */
  override def register(
    consumer: Consumer[V]
  ): Try[Boolean]

  /**
    *
    * @param consumer
    * @return
    */
  override def unregister(
    consumer: Consumer[V]
  ): Try[Boolean]
}
