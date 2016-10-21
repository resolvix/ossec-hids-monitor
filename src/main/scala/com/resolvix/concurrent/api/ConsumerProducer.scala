package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.{AbstractConsumer, AbstractProducer}

import scala.util.{Success, Try}

/**
  *
  */
object ConsumerProducer {
  /**
    *
    * @tparam T
    */
  class LocalConsumer[T]
    extends AbstractConsumer[LocalConsumer[T], T]
  {
    /**
      *
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
      Success(true)
    }
/**
    *
    * @param consumer
    * @return
    */
override def close(consumer: Consumer[T]): Try[Boolean] = super.close(consumer)
/**
    *
    * @param consumer
    * @return
    */
override def open(consumer: Consumer[T]): Try[Pipe[T]] = super.open(consumer)
/**
    *
    * @param producer
    * @return
    */
override def register[P <: Producer[T]](producer: P): Try[Boolean] = super.register(producer)

    /**
      *
      * @param producer
      * @return
      */
    override def unregister[P <: Producer[T]](producer: P): Try[Boolean] = super.unregister(producer)
}

  /**
    *
    * @tparam T
    */
  class LocalProducer[T]
    extends AbstractProducer[LocalProducer[T], T]
  {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(
      configuration: Configuration
    ): Try[Boolean] = {
      Success(true)
    }
/**
    *
    * @param producer
    * @return
    */
override def close(producer: Producer[T]): Try[Boolean] = super.close(producer)
/**
    *
    * @param producer
    * @return
    */
override def open(producer: Producer[T]): Try[Pipe[T]] = super.open(producer)
/**
    *
    * @param t
    */
override def produce(t: T): Try[Boolean] = super.produce(t)
/**
    *
    * @param producer
    * @return
    */
override def register[P <: Producer[T]](producer: P): Try[Boolean] = super.register(producer)

    /**
      *
      * @param producer
      * @return
      */
    override def unregister[P <: Producer[T]](producer: P): Try[Boolean] = super.unregister(producer)
}
}

/**
  *
  * @tparam T
  */
trait ConsumerProducer[T] {

  /**
    *
    */
  private val consumer: ConsumerProducer.LocalConsumer[T]
    = new ConsumerProducer.LocalConsumer[T]()

  /**
    *
    */
  private val producer: ConsumerProducer.LocalProducer[T]
    = new ConsumerProducer.LocalProducer[T]()

  /**
    *
    * @return
    */
  def getConsumer: Consumer[T] = this.consumer

  /**
    *
    * @return
    */
  def getProducer: Producer[P] = this.producer


  def consume: Try[C] = {
    consumer.consume
  }

  def consume(
    timeout: Int,
    unit: TimeUnit
  ): Try[C] = {
    consumer.consume(
      timeout,
      unit
    )
  }

  def produce(
    p: P
  ): Try[Boolean] = {
    producer.produce(p)
  }

}
