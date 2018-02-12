package com.resolvix.ccs

import com.resolvix.mq.api.{Reader, Writer}

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

package impl {

  /**
    * Provides generic functionality relevant to an [[Actor]] that produces
    * output for consumption by one or more consumers.
    *
    * @tparam V
    *   specifies the class of values to be transmitted from the producer to
    *   the consumer(s).
    */
  trait Producer[V]
    extends Actor[api.Producer[V], api.Consumer[V]]
      with api.Producer[V] {

    /**
      *
      * @param writerMap
      */
    class MulticastWriter(
      writerMap: Map[Int, Writer[V]]
    ) extends com.resolvix.mq.api.Writer[V] {

      /**
        *
        * @param v
        *
        * @return
        */
      override def write(
        v: V
      ): Try[Boolean] = {
        for (w: Writer[V] <- writerMap.values) {
          w.write(v)
        }
        Success(true)
      }

      override def getId: Int = {
        throw new IllegalAccessException()
      }

      override def getSelf: MulticastWriter = this
    }

    //
    //
    //
    protected var writerMap: Map[Int, Writer[V]]
    = Map[Int, Writer[V]]()

    /**
      *
      * @param consumer
      *
      * @return
      */
    override def close(
      consumer: api.Consumer[V]
    ): Try[Boolean] = {
      /*super.close(consumer)*/
      Success(true)
    }

    /**
      *
      * @param consumer
      *
      * @return
      */
    override def open(
      consumer: api.Consumer[V]
    ): Try[Reader[V]] = {
      if (super.isRegistered(consumer)) {
        try {
          Success(
            consumer.open.get
          )
        } catch {
          case NonFatal(t) =>
            Failure(t)
        }
      } else {
        Failure(new api.ConsumerNotRegisteredException)
      }
    }

    /**
      *
      * @return
      */
    def open: Try[Writer[V]] = {
      Try({
        writerMap = actors.collect({
          case (i: Int, c: api.Consumer[V]) => {
            c.open(getSelf) match {
              case Success(w: Writer[V]) =>
                (i, w)

              case Failure(t: Throwable) =>
                throw t

              case _ =>
                throw new IllegalStateException()
            }
          }
        })

        new MulticastWriter(writerMap)
      })
    }

    /**
      *
      * @param configuration
      *
      * @return
      */
    def initialise(
      configuration: api.Configuration
    ): Try[Boolean] = {
      Success(true)
    }

    /**
      *
      * @return
      */
    override def register(
      consumer: api.Consumer[V]
    ): Try[Boolean] = {
      super.register(consumer)
    }

    /**
      *
      * @return
      */
    override def unregister(
      consumer: api.Consumer[V]
    ): Try[Boolean] = {
      super.unregister(consumer)
    }
  }
}