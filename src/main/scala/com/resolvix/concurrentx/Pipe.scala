package com.resolvix.concurrentx

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeoutException}

import scala.concurrent.duration.TimeUnit
import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 16/10/2016.
  */
class Pipe[V]
  extends api.Pipe[V]
{

  /**
    *
    */
  class ProducerPipe
    extends api.ProducerPipe[V]
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
  class ConsumerPipe
    extends api.ConsumerPipe[V]
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
  private val consumerPipe: ConsumerPipe = new ConsumerPipe

  //
  //
  //
  private val producerPipe: ProducerPipe = new ProducerPipe

  /**
    *
    * @return
    */
  def getConsumerPipe: api.ConsumerPipe[V] = {
    consumerPipe
  }

  /**
    *
    * @return
    */
  def getProducerPipe: api.ProducerPipe[V] = {
    producerPipe
  }
}
