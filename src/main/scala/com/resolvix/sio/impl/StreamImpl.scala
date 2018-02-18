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
      Try(queue.offer(v))
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
      Try(queue.take)
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
      Try({
        val t: V = queue.poll(timeout, unit)
        if (t == null)
          throw new TimeoutException()
        t
      })
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
