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
  class Producer
    extends api.Pipe[V]#Producer
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
    extends api.Pipe[V]#Consumer
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
  private val consumer: api.Pipe[V]#Consumer = new Consumer

  //
  //
  //
  private val producer: api.Pipe[V]#Producer = new Producer

  /**
    *
    * @return
    */
  def getConsumer: api.Pipe[V]#Consumer = {
    consumer
  }

  /**
    *
    * @return
    */
  def getProducer: api.Pipe[V]#Producer = {
    producer
  }
}
