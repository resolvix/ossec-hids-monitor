package com.resolvix.concurrent.api

import java.util.concurrent.TimeUnit

import com.resolvix.concurrent.Pipe

import scala.util.Try

/**
  * Created by rwbisson on 16/10/16.
  */
trait ConsumerProducer[C, P] {

  /**
    *
    */
  private val consumer = new Consumer[C] {

  }

  /**
    *
    */
  private val producer = new Producer[P] {

  }

  /**
    *
    * @return
    */
  def getConsumer: Consumer[C] = this.consumer

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
