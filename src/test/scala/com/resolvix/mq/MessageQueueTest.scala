package com.resolvix.mq

import com.resolvix.mq.api.Identifiable
import com.resolvix.sio.api.{Reader, Writer}
import org.scalatest.FunSpec

import scala.util.{Failure, Success}

/**
  * Created by rwbisson on 31/10/16.
  */
class MessageQueueTest
  extends FunSpec
{

  var id: Int = 0

  class P
    extends Identifiable
  {
    val id: Int = this.synchronized { MessageQueueTest.this.id += 1; MessageQueueTest.this.id }

    def getId: Int = id
  }

  class C
    extends Identifiable
  {
    val id: Int = this.synchronized { MessageQueueTest.this.id += 1; MessageQueueTest.this.id }

    def getId: Int = id
  }

  describe("A MessageQueue") {
    it("XX") {

      val mq: MessageQueue[Int] = new MessageQueue[Int]()

      val p1: P = new P()
      val c1: C = new C()

      val p2: P = new P()
      val c2: C = new C()

      val p1c1Int: Writer[Int] = mq.getProducer(p1, c1)
      val p2c1Int: Writer[Int] = mq.getProducer(p2, c1)

      val p1c2Int: Writer[Int] = mq.getProducer(p1, c2)
      val p2c2Int: Writer[Int] = mq.getProducer(p2, c2)

      val c1Int: Reader[(P, Int)] = mq.getConsumer(c1)
      val c2Int: Reader[(P, Int)] = mq.getConsumer(c2)

      p1c1Int.write(11)



      p1c2Int.write(12)



      p2c1Int.write(21)



      p2c2Int.write(22)


      val XX: Int = 99 + 1

      val rr: (P, Int) = c1Int.read match {
        case Success(tt: (P, Int)) =>
          tt

        case Failure(t: Throwable) =>
          throw t

      }

      val rr2: (P, Int) = c1Int.read match {
        case Success(tt: (P, Int)) =>
          tt

        case Failure(t: Throwable) =>
          throw t

      }

    }
  }

}
