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
  extends FunSpec
{
  class X(
    val x: Int,
    val y: Int
  )
  {

  }

  class C
    extends Consumer[C, P, X]
  {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: C = this
  }

  class P
    extends Producer[P, C, X]
  {
/**
    *
    * @param configuration
    * @return
    */
override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: P = this
}

  describe("An instance of a Producer / Consumer ") {
    describe("should be capabile of being created") {
      it("each should be capable of being cross-registered") {
        val consumer: C = new C

        val producer: P = new P

        val b1: Try[Boolean] = consumer.register(producer)
        val b2: Try[Boolean] = producer.register(consumer)

        val tryWriter: Try[Writer[_, X]] = producer.open

        val writer: Writer[_, X] = tryWriter match {
          case Success(w: Writer[_, X]) => w.asInstanceOf[Writer[_, X]]
          case Failure(t: Throwable) => throw t
        }

        val tryReader: Try[Reader[_, X]] = consumer.open

        val reader: Reader[_, X] = tryReader match {
          case Success(r: Reader[_, X]) => r.asInstanceOf[Reader[_, X]]
          case Failure(t: Throwable) => throw t
          case _ => throw new IllegalStateException()
        }

        writer.write(new X(1, 1))
        writer.write(new X(2, 2))
        writer.write(new X(3, 3))

        val r1: Try[(Int, X)] = reader.read
        println(r1.get._2.x)

        val r2: Try[(Int, X)] = reader.read
        println(r2.get._2.x)

        val r3: Try[(Int, X)] = reader.read
        println(r3.get._2.x)

      }
    }
  }


}
