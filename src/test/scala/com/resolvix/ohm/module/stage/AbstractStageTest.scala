package com.resolvix.ohm.module.stage

import com.resolvix.ohm.module.api.{Module, ModuleDescriptor}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

import scala.util.Try

class AbstractStageTest extends FlatSpec with MockFactory {

  class MockResult(
    val results: Array[Boolean]
  ) extends StageResult[Boolean](results)

  class OddEvenStageDescriptor extends AbstractStageDescriptor[Integer, Integer, MockResult] {
    override protected def getConfigurations: Array[String] = ???

    override protected def newModule(config: Map[String, Any]): Try[Module[Integer, Integer, MockResult]] = ???
  }

  class OddEvenStage extends AbstractStage[OddEvenStage, Integer, Integer, MockResult] {

    override def close(): Try[Boolean] = {
      Try(true)
    }

    override def getId: Int = 1

    override def getDescriptor: ModuleDescriptor[Integer, Integer, MockResult] = {
      new OddEvenStageDescriptor
    }

    override def flush(): Try[MockResult] = {
      Try(new MockResult(Array.emptyBooleanArray))
    }

    override def initialise(): Try[Boolean] = {
      Try(true)
    }

    override def open(): Try[Boolean] = {
      Try(true)
    }

    override def terminate(): Try[Boolean] = {
      Try(true)
    }

    override protected def transform(input: Integer): Try[(Integer, MockResult)] = {
      Try((input, new MockResult(Array (input % 2 == 0))))
    }
  }

  val oddEvenStage = new OddEvenStage

  "An OddEvenStage" should "generate true if provided with an odd value as input" in {
    val tried: Try[Boolean] = oddEvenStage.consume(1)
  }

  it should "generate false if provided with an even value as input" in {
    val tried: Try[Boolean] = oddEvenStage.consume(2)
  }
}
