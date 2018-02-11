package com.resolvix.mq

import java.util.concurrent.TimeUnit

import com.resolvix.mq.api.Identifiable
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.mq.api.MessageQueue
import org.junit.Assert
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.matchers.Matcher

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

      val mq: MessageQueue[Int] = MessageQueueFactory.newMessageQueue()

      val p1: P = new P()
      val c1: C = new C()

      val p2: P = new P()
      val c2: C = new C()

      val p1c1Int: Writer[Int] = mq.getWriter(p1, c1)
      val p2c1Int: Writer[Int] = mq.getWriter(p2, c1)

      val p1c2Int: Writer[Int] = mq.getWriter(p1, c2)
      val p2c2Int: Writer[Int] = mq.getWriter(p2, c2)

      val c1Int: Reader[Int] = mq.getReader(c1)
      val c2Int: Reader[Int] = mq.getReader(c2)

      p1c1Int.write(11)

      p1c2Int.write(12)

      p2c1Int.write(21)

      p2c2Int.write(22)

      p1c1Int.write(13)

      p1c2Int.write(14)

      p2c1Int.write(23)

      p2c2Int.write(24)

      def RR: (Int, Int) = c1Int.read(5, TimeUnit.SECONDS) match {
        case Success(tt: (Int, Int)) =>
          tt

        case Failure(t: Throwable) =>
          throw t

      }

      def check(expected: (Int, Int), actual: (Int, Int)): Unit = {
        println(actual._1 + " -> " + actual._2)
        assert(actual._1.equals(expected._1))
        assert(actual._2.equals(expected._2))
      }

      check((1, 11), RR)
      check((3, 21), RR)
      check((1, 13), RR)
      check((3, 23), RR)

      def RR2: (Int, Int) = c2Int.read(5, TimeUnit.SECONDS) match {
        case Success(tt: (Int, Int)) =>
          tt

        case Failure(t: Throwable) =>
          throw t

      }

      check((4, 12), RR2)
      check((6, 22), RR2)
      check((4, 14), RR2)
      check((6, 24), RR2)
    }
  }

}
