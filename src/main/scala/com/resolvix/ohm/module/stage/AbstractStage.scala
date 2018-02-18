package com.resolvix.ohm.module.stage
import com.resolvix.ccs.api.{Consumer, Producer, ProducerConsumer, RunnableProducerConsumer}
import com.resolvix.ccs.impl.RunnableConsumerProducer
import com.resolvix.ccs.impl.RunnableConsumerProducer.Operation
import com.resolvix.ccs.impl.RunnableConsumerProducer.Operation.Operation
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
  private class InputRunnableConsumerProducer
    extends com.resolvix.ccs.api.ConsumerProducer[InputRunnableConsumerProducer, I, R]
    with com.resolvix.ccs.impl.RunnableConsumerProducer[InputRunnableConsumerProducer, I, R] {

    override val operation: Operation = Operation.AsynchronousConsumerSynchronousProducer

    /**
      * Consumes an input object of type [[I]] upon receipt.
      *
      * @param c
      *   the consumable input object
      *
      * @return
      *   a value of type [[Try[Boolean]]] indicating whether consumption of
      *   the input object was successful and, if not, indicating the cause
      *   of any failure
      */
    override def doConsume(c: I): Try[Boolean] = {
      AbstractStage.this.consume(c)
    }
  }

  /**
    * Provides an implementation of a runnable producer consumer message queue
    * pair for use by the back end processing element of the abstract stage.
    */
  private class OutputRunnableProducerConsumer
    extends com.resolvix.ccs.api.ProducerConsumer[OutputRunnableProducerConsumer, O, R]
    with com.resolvix.ccs.impl.RunnableProducerConsumer[OutputRunnableProducerConsumer, O, R] {

    override val operation: Operation = Operation.AsynchronousConsumerSynchronousProducer

    /**
      * Consumes a result object of type [[R]] upon receipt.
      *
      * @param r
      *   the consumable result object
      *
      * @return
      *   a value of type [[Try[Boolean]]] indicating whether consumption of
      *   the result object was successful and, if not, indicating the cause
      *   of any failure
      */
    override def doConsume(r: R): Try[Boolean] = {
      AbstractStage.this.consume(r)
    }
  }

  private val inputConsumerProducer:  InputRunnableConsumerProducer
    = new  InputRunnableConsumerProducer

  private val outputConsumerProducer: OutputRunnableProducerConsumer
    = new OutputRunnableProducerConsumer

  //private val consumerI: Consumer[I] = inputConsumerProducer.getConsumer

  //private val readerI: Reader[I] = consumerI.open.get

  private val producerR: Producer[R] = inputConsumerProducer.getProducer

  private val writerR: Writer[R] = producerR.open.get

  private val producerO: Producer[O] = outputConsumerProducer.getProducer

  private val writerO: Writer[O] = producerO.open.get

  //private val consumerR: Consumer[R] = outputConsumerProducer.getConsumer

  //private val readerR: Reader[R] = consumerR.open.get

  protected def transform(input: I): Try[(O, R)]

  def consume(input: I): Try[Boolean] = {
    transform(input) match {
      case Success((output: O @unchecked, result: R @unchecked)) =>
        Try({
          // TODO Implement logic to return Failure on first failure; possibly a for comprehension
          produce(result)
          produce(output)
          true
        })

      case Failure(t: Throwable) =>
        Failure(t)

    }
  }

  private def produce(result: R): Try[Boolean] = {
    writerR.write(result)
  }

  private def produce(output: O): Try[Boolean] = {
    writerO.write(output)
  }

  def consume(result: R): Try[Boolean] = {
    produce(result)
  }
}
