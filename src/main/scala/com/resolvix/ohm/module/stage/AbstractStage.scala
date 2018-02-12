package com.resolvix.ohm.module.stage
import com.resolvix.ccs.api.{Consumer, Producer, ProducerConsumer, RunnableProducerConsumer}
import com.resolvix.ccs.impl.RunnableConsumerProducer
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.ohm.module.api.ModuleDescriptor
import com.resolvix.ohm.module.stage

import scala.util.{Failure, Success, Try}

/**
  *
  * @tparam AS
  * @tparam I
  * @tparam O
  * @tparam R
  */
private[stage] abstract class AbstractStage[AS <: AbstractStage[AS, I, O, R], I, O, R <: api.StageResult[_]]
  extends api.Stage[I, O, R] {
  /**
    * Provides an implementation of a runnable consumer producer message queue
    * pair for use by the front end processing element of the abstract stage.
    */
  private class FrontEndRunnableConsumerProducer
    extends com.resolvix.ccs.api.ConsumerProducer[FrontEndRunnableConsumerProducer, I, R]
    with com.resolvix.ccs.impl.RunnableConsumerProducer[FrontEndRunnableConsumerProducer, I, R] {

    /**
      * Consumes an object of type [[I]] upon receipt.
      *
      * @param c
      * @return
      */
    override def doConsume(c: I): Try[Boolean] = {
      val tryR: Try[R] = AbstractStage.this.consume(c)
      tryR match {
        case Success(r: R @unchecked) =>
          //Try(produce(r));
          Failure(new UnsupportedOperationException)

        case _ =>
          Failure(new Exception)
      }
    }

    override def doProduce(): Try[R] = {
      Failure(new Exception())
    }
  }

  /**
    * Provides an implementation of a runnable producer consumer message queue
    * pair for use by the back end processing element of the abstract stage.
    */
  private class BackEndRunnableProducerConsumer
    extends com.resolvix.ccs.api.ProducerConsumer[BackEndRunnableProducerConsumer, O, R]
    with com.resolvix.ccs.impl.RunnableProducerConsumer[BackEndRunnableProducerConsumer, O, R] {
    override def doConsume(c: R): Try[Boolean] = {
      Failure(new UnsupportedOperationException)
    }

    override def doProduce(): Try[O] = {
      Failure(new UnsupportedOperationException)
    }
  }

  private val frontEndConsumerProducer: FrontEndRunnableConsumerProducer
    = new FrontEndRunnableConsumerProducer

  private val backEndConsumerProducer: BackEndRunnableProducerConsumer
    = new BackEndRunnableProducerConsumer

  private val consumer: Consumer[I] = frontEndConsumerProducer.getConsumer

  private val reader: Reader[I] = consumer.open.get

  private val producer: Producer[O] = backEndConsumerProducer.getProducer

  private val writer: Writer[O] = producer.open.get

  override protected def produce(output: O): Try[Boolean] = {
    Success(true)
  }
}
