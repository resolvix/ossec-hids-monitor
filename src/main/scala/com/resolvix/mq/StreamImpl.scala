package com.resolvix.mq

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeoutException}

import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/2016.
  */
class StreamImpl[V]
  extends api.Stream[V]
{

  /**
    *
    */
  class Producer
    extends super.Producer
  {
    /**
      *
      * @param v
      * @return
      */
    override def write(
      v: V
    ): Try[Boolean] = {
      try {
        Success(queue.offer(v))
      } catch {
        case t: Throwable =>
          Failure(t)
      }
    }
  }

  /**
    *
    */
  class Consumer
    extends super.Consumer
  {
    /**
      *
      * @return
      */
    override def read: Try[V] = {
      try {
        Success(queue.take)
      } catch {
        case e: InterruptedException =>
          Failure(e)
      }
    }

    /**
      *
      * @param timeout
      * @param unit
      * @return
      */
    override def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[V] = {
      try {
        val t: V = queue.poll(timeout, unit)
        if (t != null) {
          Success(t)
        } else {
          Failure(new TimeoutException)
        }
      } catch {
        case e: InterruptedException =>
          Failure(e)
      }
    }
  }

  //
  //
  //
  private val queue: BlockingQueue[V]
    = new LinkedBlockingQueue[V]()

  //
  //
  //
  private val consumer: super.Consumer = new Consumer

  //
  //
  //
  private val producer: super.Producer = new Producer

  /**
    *
    * @return
    */
  def getConsumer: super.Consumer = {
    consumer
  }

  /**
    *
    * @return
    */
  def getProducer: super.Producer = {
    producer
  }
}
