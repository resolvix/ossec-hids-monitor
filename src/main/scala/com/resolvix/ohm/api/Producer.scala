package com.resolvix.ohm.api

import scala.collection.mutable.ListBuffer

trait Producer[T] {
  //
  //
  //
  private val consumers: ListBuffer[Consumer[T]]
    = ListBuffer[Consumer[T]]()

  /**
    *
    */
  def close(): Unit = {
    consumers.foreach(
      (consumer: Consumer[T]) =>
        consumer.close(this)
    )
  }

  /**
    *
    * @param consumer
    */
  def deregister(
    consumer: Consumer[T]
  ): Unit = {
    consumers -= consumer
  }

  /**
    *
    */
  def open(): Unit = {
    consumers.foreach(
      (consumer: Consumer[T]) =>
        consumer.open(this)
    )
  }

  /**
    *
    * @param consumer
    */
  def register(
    consumer: Consumer[T]
  ): Unit = {
    consumers += consumer
  }

  /**
    *
    * @param t
    */
  def write(
    t: T
  ): Unit = {
    consumers.foreach(
      (consumer: Consumer[T]) =>
        consumer.receive(t)
    )
  }
}
