package com.resolvix.ohm.module.connector

import com.resolvix.ohm.module.AbstractModule
import com.resolvix.ohm.module.api.{Module, ModuleDescriptor, Result}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

import scala.util.Try

class SynchronousConnectorTest extends FlatSpec with MockFactory {

  class MockResult(
    private val result: Boolean
  ) extends Result {
    def get: Boolean = result
  }

  class MockModule extends AbstractModule[MockModule, Integer, Integer, MockResult] {
    override def consume(input: Integer): Try[Boolean] = ???

    override def consume(result: MockResult): Try[Boolean] = ???

    override def flush(): Try[MockResult] = ???

    override def getId: Int = ???

    override def getDescriptor: ModuleDescriptor[Integer, Integer, MockResult] = ???
  }

  val inputModule = stub[MockModule]
  val outputModule = stub[MockModule]

  val synchronousConnector = new SynchronousConnector[MockModule, MockModule, Integer, MockResult](inputModule, outputModule)

  val mockResultTrue = new MockResult(true)
  val mockResultFalse = new MockResult(false)

  "A SynchronousConnector" should "connect an input module to an output module" in {

   // (inputModule.consume(_: Integer)) expects(1) then(2) returns(Try(true))
    (inputModule.consume(_: MockResult)).when(*).twice().returns(Try(true))

    //(outputModule.consume(_: Boolean)) expects(false) then(true)
    (outputModule.consume(_: Integer)).when(*).twice().returns(Try(true))

    synchronousConnector.send(1)
    synchronousConnector.send(2)

    synchronousConnector.receive(mockResultTrue)
    synchronousConnector.receive(mockResultFalse)

    (inputModule.consume(_: MockResult)).verify(mockResultTrue)
    (inputModule.consume(_: MockResult)).verify(mockResultFalse)
    (outputModule.consume(_: Integer)).verify(Int.box(1))
    (outputModule.consume(_: Integer)).verify(Int.box(2))
  }
}
