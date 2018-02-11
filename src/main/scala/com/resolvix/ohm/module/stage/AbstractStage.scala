package com.resolvix.ohm.module.stage
import com.resolvix.ccs.api.{Consumer, Producer, ProducerConsumer, RunnableProducerConsumer}
import com.resolvix.ccs.impl.RunnableConsumerProducer
import com.resolvix.mq.api.{Reader, Writer}
import com.resolvix.ohm.module.api.ModuleDescriptor
import com.resolvix.ohm.module.stage

import scala.util.{Failure, Success, Try}

private object AbstractStage {
  /**
    * Provides a generic implementation of a runnable consumer producer
    * message queue pair for use by the front end processing element of
    * the abstract stage.
    *
    * @tparam I
    *   indicates the type of inputs the consumer producer is expected
    *   to enable the consumer to read
    *
    * @tparam O
    *   indicates the type of outputs the consumer producer is expected
    *   to enable the producer to write
    */
  class FrontEndRunnableConsumerProducer[I, R <: api.StageResult[_]](
    private val stage: api.Stage[I, _, R]
  ) extends com.resolvix.ccs.api.ConsumerProducer[FrontEndRunnableConsumerProducer[I, R], I, R]
      with com.resolvix.ccs.impl.RunnableConsumerProducer[FrontEndRunnableConsumerProducer[I, R], I, R] {

    /**
      * Consumes an object of type [[I]] upon receipt.
      *
      * @param c
      * @return
      */
    override def doConsume(c: I): Try[Boolean] = {
      val tryR: Try[R] = stage.consume(c)
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

  class BackEndRunnableProducerConsumer[O, R <: api.StageResult[_]](
    private val stage: api.Stage[_, O, R]
  ) extends com.resolvix.ccs.api.ProducerConsumer[BackEndRunnableProducerConsumer[O, R], O, R]
      with com.resolvix.ccs.impl.RunnableProducerConsumer[BackEndRunnableProducerConsumer[O, R], O, R] {
    override def doConsume(c: R): Try[Boolean] = {
      Failure(new UnsupportedOperationException)
    }

    override def doProduce(): Try[O] = {
      Failure(new UnsupportedOperationException)
    }

    //override def crossregister[PC <: ProducerConsumer[PC, O, R]](producerConsumer: PC): Try[Boolean] = ???
  }
}

/**
  *
  * @tparam AS
  * @tparam I
  * @tparam O
  * @tparam R
  */
private[stage] abstract class AbstractStage[AS <: AbstractStage[AS, I, O, R], I, O, R <: api.StageResult[_]]
  extends api.Stage[I, O, R] {

  private val frontEndConsumerProducer: AbstractStage.FrontEndRunnableConsumerProducer[I, R]
    = new AbstractStage.FrontEndRunnableConsumerProducer[I, R](this)

  private val backEndConsumerProducer: AbstractStage.BackEndRunnableProducerConsumer[O, R]
    = new AbstractStage.BackEndRunnableProducerConsumer[O, R](this)

  private val consumer: Consumer[I] = frontEndConsumerProducer.getConsumer

  private val reader: Reader[I] = consumer.open.get

  private val producer: Producer[O] = backEndConsumerProducer.getProducer

  private val writer: Writer[O] = producer.open.get

  override protected def produce(output: O): Try[Boolean] = {
    Success(true)
  }
}
