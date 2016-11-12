package com.resolvix.ccs.runnable

import com.resolvix.mq.api.{Reader, Writer}
import org.scalatest.FunSpec

import scala.util.Try

class ProducerConsumerTest
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

  }

  class CP
    extends com.resolvix.ccs.runnable.ConsumerProducer[CP, X, Y]
  {

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


    it("should be possible to cross-registerd the ProducerConsumer with the ConsumerProducer") {
      //val b1: Try[Boolean] = producerConsumer.crossregister(consumerProducer)
    }

    /**it("should be possible to open the producer and consumer channels") {
      writerX = producerConsumer.getProducer.open.get
      readerY = producerConsumer.getConsumer.

    }*/

    it("should be possible to start the producer / consumer, and consumer / producer threads") {



    }

    it("should be possible to write values to the producer") {



    }

    it("should be possible to read values from the consumer") {

    }
  }
}
