package com.resolvix.ohm.module.stage
import com.resolvix.ccs.api.{Consumer, Producer}
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.ohm.module.api.ModuleDescriptor

import scala.util.{Failure, Success, Try}

object AbstractStage {
  class LocalConsumerProducer[I, O]
    extends com.resolvix.ccs.api.ConsumerProducer[LocalConsumerProducer[I, O], I, O]
      with com.resolvix.ccs.runnable.ConsumerProducer[LocalConsumerProducer[I, O], I, O] {
    override def doConsume(c: I): Try[Boolean] = {
      Success(false)

    }

    override def doProduce(): Try[O] = {
      Failure(new Exception())
    }
  }
}

/**
  *
  * @tparam AI
  * @tparam I
  * @tparam O
  */
abstract class AbstractStage[AI <: AbstractStage[AI, I, O, R], I, O, R <: api.StageResult[_]]
  extends api.Stage[I, O, R] {

  private val localConsumerProducer: AbstractStage.LocalConsumerProducer[I, O]
    = new AbstractStage.LocalConsumerProducer[I, O]();

  private val consumer: Consumer[I] = localConsumerProducer.getConsumer

  private val reader: Reader[I] = consumer.open.get

  private val producer: Producer[O] = localConsumerProducer.getProducer

  private val writer: Writer[O] = producer.open.get

  override protected def produce(output: O): Try[Boolean] = {
    //writer.
    Success(true)
  }
}
