package com.resolvix.sio.impl

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue, TimeoutException}

import com.resolvix.sio.api

import scala.concurrent.duration.TimeUnit
import scala.util.control.NonFatal
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
  class Writer
    extends api.Writer[V]
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
        case NonFatal(t) =>
          Failure(t)
      }
    }
  }

  /**
    *
    */
  class Reader
    extends api.Reader[V]
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
  private val reader: Reader = new Reader

  //
  //
  //
  private val writer: Writer = new Writer

  /**
    *
    * @return
    */
  def getReader(): Reader = {
    reader
  }

  /**
    *
    * @return
    */
  def getWriter: Writer = {
    writer
  }
}
