package com.resolvix.concurrentx.api
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
  def getConsumerPipe: ConsumerPipe[V]

  /**
    *
    * @return
    */
  def getProducerPipe: ProducerPipe[V]
}
