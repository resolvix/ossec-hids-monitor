package com.resolvix.concurrentx

import com.resolvix.concurrentx.api.Configuration
import com.resolvix.mq.MessageQueue
import com.resolvix.mq.api.{Reader, Writer}
import org.scalatest.FunSpec

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 07/11/16.
  */
class ProducerConsumerTest
  extends FunSpec {

  class X(
    val x: Int,
    val y: Int
  ) {

  }

  class C
    extends Consumer[C, P, X] {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: C = this
  }

  class P
    extends Producer[P, C, X] {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: P = this
  }

  describe("For instance of a Producer / Consumer ") {

    val consumer: C = new C

    val producer: P = new P

    var writer: Writer[_, X] = null

    var reader: Reader[_, X] = null

    it("should be possible to register the Producer with the Consumer") {
      val b1: Try[Boolean] = consumer.register(producer)
    }

    it("should be possible to register the Consumer with the Producer") {
      val b2: Try[Boolean] = producer.register(consumer)
    }

    it("the producer should be capable of being opened to obtain a writer") {
      val tryWriter = producer.open: Try[Writer[_, X]]

      writer = tryWriter match {
        case Success(w: Writer[_, X]) => w.asInstanceOf[Writer[_, X]]
        case Failure(t: Throwable) => throw t
      }
    }

    it("the consumer should be capable of being opened to obtain a reader") {
      val tryReader: Try[Reader[_, X]] = consumer.open

      reader = tryReader match {
        case Success(r: Reader[_, X]) => r.asInstanceOf[Reader[_, X]]
        case Failure(t: Throwable) => throw t
        case _ => throw new IllegalStateException()
      }
    }

    it("should be possible to write values to a writer") {
      writer.write(new X(1, 1))
      writer.write(new X(2, 2))
      writer.write(new X(3, 3))
    }

    it("should be possible to read values from a reader") {
        val r1: Try[(Int, X)] = reader.read
        println(r1.get._2.x)

        val r2: Try[(Int, X)] = reader.read
        println(r2.get._2.x)

        val r3: Try[(Int, X)] = reader.read
        println(r3.get._2.x)
    }
  }


}
