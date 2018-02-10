package com.resolvix.ccs.runnable

import com.resolvix.mq.api.{Reader, Writer}
import org.scalatest.FunSpec

import scala.util.{Failure, Success, Try}

class ProducerXConsumerXTest
  extends FunSpec
{
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

  class PC
    extends com.resolvix.ccs.runnable.ProducerConsumer[PC, X, Y]
  {
    override def doConsume(c: Y): Try[Boolean] = ???

    override def doProduce(): Try[X] = ???
  }

  class CP
    extends com.resolvix.ccs.runnable.ConsumerProducer[CP, X, Y]
  {
    override def doConsume(c: X): Try[Boolean] = {
      println("CP::doConsume: X(" + c.x + ", " + c.y + ")")
      getProducer.open.get.write(new Y(c.y, c.x))
      Success(true)
    }

    override def doProduce(): Try[Y] = {
      Success(new Y(99,99))
    }
  }

  describe("For an instance of a runnable ProducerConsumer and ConsumerProducer") {

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

    var thread1: Thread = null

    var thread2: Thread = null

    it("should be possible to cross-registered the ProducerConsumer with the ConsumerProducer") {
      val b1: Try[Boolean] = producerConsumer.crossRegister(consumerProducer)
    }

    it("should be possible to open the producer and consumer channels") {
      writerX = producerConsumer.getProducer.open.get
      assert(writerX != null, "writerX is null")

      readerY = producerConsumer.getConsumer.open match {
        case Success(readerY: Reader[Y]) => readerY
        case Failure(t: Throwable) => throw t
      }

      assert(readerY != null, "readerY is null")
    }

    it("should be possible to start the producer / consumer, and consumer / producer threads") {

      consumerX = consumerProducer.getConsumer

      producerY = consumerProducer.getProducer

      thread1 = new Thread(consumerX)
      thread1.start()

      thread2 = new Thread(producerY)
      //thread2.start()

    }

    it("should be possible to write values to the producer") {

      writerX.write(new X(1,9))
      writerX.write(new X(2,8))
      writerX.write(new X(3,7))
      writerX.write(new X(4,6))
      writerX.write(new X(5,5))

    }

    it("should be possible to read values from the consumer") {

      val r1 = readerY.read
      println(r1.get._2.x)

      val r2 = readerY.read
      println(r2.get._2.x)

      val r3 = readerY.read
      println(r3.get._2.x)

      val r4 = readerY.read
      println(r4.get._2.x)

      val r5 = readerY.read
      println(r5.get._2.x)

    }

    it("should be possible to stop the consumer / producer") {

      consumerX.finish()
      thread1.join()

      producerY.finish()
      thread2.join()
    }
  }
}
