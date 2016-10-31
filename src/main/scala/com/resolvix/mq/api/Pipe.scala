package com.resolvix.mq.api

import scala.concurrent.duration.TimeUnit
import scala.util.Try

/**
  * Created by rwbisson on 20/10/16.
  */
trait Pipe[V]
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
