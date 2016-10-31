package com.resolvix.mq.api

import com.resolvix.concurrentx.api.Transport

import scala.concurrent.duration.TimeUnit
import scala.util.Try

/**
  * Created by rwbisson on 20/10/16.
  */
trait Pipe[V]
  extends Transport[V]
{
  /**
    *
    * @return
    */
  def getConsumer: Consumer[V]

  /**
    *
    * @return
    */
  def getProducer: Producer[V]
}
