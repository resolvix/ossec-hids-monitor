package com.resolvix.concurrent

import com.resolvix.concurrent.api.{Consumer, Producer}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

object AbstractProducer {
  sealed class Consumer[T](
    private val consumer: api.Consumer[T],
    private val sink: Consumer.Sink[T]
  ) {

    private val id: Int = generateId()

    /**
      *
      * @return
      */
    def getConsumer: Consumer[T] = {
      consumer
    }

    /**
      *
      * @return
      */
    def getId: Int = {
      id
    }

    /**
      *
      * @return
      */
    def getSink: Consumer.Sink[T] = {
      sink
    }
  }

  //
  //
  //
  var id: Int = 0

  /**
    *
    * @return
    */
  def generateId(): Int = {
    id.synchronized {
      id += 1
      id
    }
  }
}

/**
  * Created by rwbisson on 19/10/16.
  */
trait AbstractProducer[T]
  extends Producer[T]
{
  //
  //
  //
  private val consumers: ListBuffer[AbstractProducer.Consumer[T]]
  = ListBuffer[AbstractProducer.Consumer[T]]()

  /**
    *
    */
  def close(): Unit = {
    consumers.foreach(
      (consumer: AbstractProducer.Consumer[T]) =>
        consumer.getSink.close(this)
    )
  }

  /**
    *
    */
  def open(): Unit = {
    consumers.foreach(
      (consumer: AbstractProducer.Consumer[T]) =>
        consumer.getSink.open(this)
    )
  }

  /**
    *
    * @param t
    */
  def produce(
    t: T
  ): Try[Boolean] = {
    consumers.foreach(
      (c: AbstractProducer.Consumer[T]) => {
        try {
          c.getSink.write(t)
        } catch {
          case t: Throwable =>
          //
          //  Do nothing
          //
        }
      }
    )
    Success(true)
  }

  /**
    *
    * @param consumer
    */
  def register(
    consumer: Consumer[T]
  ): Try[Boolean] = {
    consumer.register(this) match {
      case Success(sinkT: Consumer.Sink[T]) => {
        consumers += new AbstractProducer.Consumer[T](
          consumer,
          sinkT
        )
        Success(true)
      }

      case Failure(t: Throwable) =>
        Failure(t)
    }
  }

  /**
    *
    * @param consumer
    */
  def unregister(
    consumer: Consumer[T]
  ): Try[Boolean] = {
    consumers -= consumer
    Success(true)
  }
}
