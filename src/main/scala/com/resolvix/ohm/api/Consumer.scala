package com.resolvix.ohm.api

import scala.collection.mutable
import scala.util.Try

/**
  * Created by rwbisson on 16/10/16.
  */
trait Consumer[T] {

  private val inputQueue: mutable.Queue[T]
    = mutable.Queue[T]()

  /**
    *
    * @param producer
    */
  def open(
    producer: Producer[T]
  ): Unit = { }

  /**
    *
    * @param t
    */
  def receive(
    t: T
  ): Unit = {
    inputQueue.enqueue(t)
  }

  /**
    *
    * @return
    */
  def read: T = {
    inputQueue.dequeue()
  }

  /**
    *
    * @param producer
    */
  def close(
    producer: Producer[T]
  ): Unit = { }
}
