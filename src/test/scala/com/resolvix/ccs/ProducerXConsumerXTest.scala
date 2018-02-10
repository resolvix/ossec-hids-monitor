package com.resolvix.ccs

import com.resolvix.ccs.api.Configuration
import com.resolvix.mq.MessageQueue
import com.resolvix.mq.api.{Reader, Writer}
import org.scalatest.FunSpec

import scala.util.{Failure, Success, Try}

/**
  * Created by rwbisson on 07/11/16.
  */
class ProducerXConsumerXTest
  extends FunSpec {

  class X(
    val x: Int,
    val y: Int
  ) {

  }

  class Y(
    val x: Int,
    val y: Int
  ) {

  }

  class C
    extends Consumer[X] {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: C = this
  }

  class P
    extends Producer[X] {
    /**
      *
      * @param configuration
      * @return
      */
    override def initialise(configuration: Configuration): Try[Boolean] = ???

    override protected def getSelf: P = this
  }


  class PC
    extends ProducerConsumer[PC, X, Y]
  {

  }

  class CP
    extends ConsumerProducer[CP, X, Y]
  {

  }

  describe("For instance of a Producer / Consumer ") {

    val consumer: C = new C

    val producer: P = new P

    var writer: Writer[X] = null

    var reader: Reader[X] = null

    it("should be possible to register the Producer with the Consumer") {
      val b1: Try[Boolean] = consumer.register(producer)
    }

    it("should be possible to register the Consumer with the Producer") {
      val b2: Try[Boolean] = producer.register(consumer)
    }

    it("the producer should be capable of being opened to obtain a writer") {
      val tryWriter = producer.open: Try[Writer[X]]

      writer = tryWriter match {
        case Success(w: Writer[X]) => w.asInstanceOf[Writer[X]]
        case Failure(t: Throwable) => throw t
      }
    }

    it("the consumer should be capable of being opened to obtain a reader") {
      val tryReader: Try[Reader[X]] = consumer.open

      reader = tryReader match {
        case Success(r: Reader[X]) => r.asInstanceOf[Reader[X]]
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

  describe("For instance of a ProducerConsumer / ConsumerProducer") {

    val producerConsumer: PC = new PC

    val consumerProducer: CP = new CP

    var writerX: Writer[X] = null

    var readerX: Reader[X] = null

    var writerY: Writer[Y] = null

    var readerY: Reader[Y] = null

    var producerX: Producer[X] = null

    var consumerX: Consumer[X] = null

    var producerY: Producer[Y] = null

    var consumerY: Consumer[Y] = null

    it("should be possible to cross-register the ProducerConsumer with the ConsumerProducer") {
      val b1: Try[Boolean] = producerConsumer.crossRegister(consumerProducer)
      //val b2: Try[Boolean] = consumerProducer.crossregister(producerConsumer)
    }

    it("should be possible to cross-open the relevant producer / consumer channels") {
      val tryWriterX = producerConsumer.getProducer.open

      writerX = tryWriterX.get

      val tryReaderX = consumerProducer.getConsumer.open

      readerX = tryReaderX.get

      val tryWriterY = consumerProducer.getProducer.open

      writerY = tryWriterY.get

      val tryReaderY = producerConsumer.getConsumer.open

      readerY = tryReaderY.get
    }

    it("should be possible to write values to the producer / consumer") {

      writerX.write(new X(1, 9))
      writerX.write(new X(2, 8))
      writerX.write(new X(3, 7))

    }

    it("should be possible to read and write values from the consumer / producer") {

      def process(rX: Reader[X], wY: Writer[Y]): Unit = {
        rX.read match {
          case Success(r: (Int, X)) =>
            wY.write(
              new Y(r._2.y, r._2.x)
            )

          case Failure(t: Throwable) =>
            throw t
        }
      }

      process(readerX, writerY)
      process(readerX, writerY)
      process(readerX, writerY)

    }

    it("should be possible to read values from the producer / consumer") {

      val r1: Try[(Int, Y)] = readerY.read
      println(r1.get._2.x)

      val r2: Try[(Int, Y)] = readerY.read
      println(r2.get._2.x)

      val r3: Try[(Int, Y)] = readerY.read
      println(r3.get._2.x)

    }
  }

}
