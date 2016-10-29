package com.resolvix.concurrentx.api
import scala.concurrent.duration.TimeUnit
import scala.util.Try

/**
  * Created by rwbisson on 20/10/16.
  */
trait Pipe[V]
  extends Transport[V]
{
  trait Consumer
  {
    /**
      *
      * @return
      */
    def read: Try[V]

    /**
      *
      * @param timeout
      * @param unit
      * @return
      */
    def read(
      timeout: Int,
      unit: TimeUnit
    ): Try[V]
  }

  trait Producer
  {
    /**
      *
      * @param v
      * @return
      */
    def write(
      v: V
    ): Try[Boolean]
  }

  /**
    *
    * @return
    */
  def getConsumer: Consumer

  /**
    *
    * @return
    */
  def getProducer: Producer}
