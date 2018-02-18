package com.resolvix.ccs

import scala.util.Try

import com.resolvix.ccs.impl.RunnableConsumerProducer.Operation
import com.resolvix.ccs.impl.RunnableConsumerProducer.Operation.Operation
import com.resolvix.ohm.module.stage.AbstractStage

package impl {

  trait DuplexConsumerProducer[DCP <: DuplexConsumerProducer[DCP, LC, LP, RC, RP], LC, LP, RC, RP]
    extends api.DuplexConsumerProducer[DCP, LC, LP, RC, RP] {

    /**
      * Provides an implementation of a runnable consumer producer message queue
      * pair for use by the front end processing element of the abstract stage.
      */
    protected class LeftRunnableConsumerProducer
      extends com.resolvix.ccs.api.ConsumerProducer[LeftRunnableConsumerProducer, LC, LP]
        with com.resolvix.ccs.impl.RunnableConsumerProducer[LeftRunnableConsumerProducer, LC, LP] {

      override val operation: Operation = Operation.AsynchronousConsumerSynchronousProducer

      /**
        * Consumes an input object of type [[LC]] upon receipt.
        *
        * @param lc
        *   the consumable input object
        *
        * @return
        *   a value of type [[Try[Boolean]]] indicating whether consumption of
        *   the input object was successful and, if not, indicating the cause
        *   of any failure
        */
      override def doConsume(lc: LC): Try[Boolean] = {
        DuplexConsumerProducer.this.doLeftConsume(lc)
      }
    }

    /**
      * Provides an implementation of a runnable producer consumer message queue
      * pair for use by the back end processing element of the abstract stage.
      */
    protected class RightRunnableProducerConsumer
      extends com.resolvix.ccs.api.ProducerConsumer[RightRunnableProducerConsumer, RP, RC]
        with com.resolvix.ccs.impl.RunnableProducerConsumer[RightRunnableProducerConsumer, RP, RC] {

      override val operation: Operation = Operation.AsynchronousConsumerSynchronousProducer

      /**
        * Consumes a result object of type [[RC]] upon receipt.
        *
        * @param rc
        *   the consumable result object
        *
        * @return
        *   a value of type [[Try[Boolean]]] indicating whether consumption of
        *   the result object was successful and, if not, indicating the cause
        *   of any failure
        */
      override def doConsume(rc: RC): Try[Boolean] = {
        DuplexConsumerProducer.this.doRightConsume(rc)
      }
    }

    protected val createLeftRunnableConsumerProducer: LeftRunnableConsumerProducer = {
      new LeftRunnableConsumerProducer
    }

    protected val createRightRunnableProducerConsumer: RightRunnableProducerConsumer = {
      new RightRunnableProducerConsumer
    }

    protected val leftRunnableConsumerProducer: LeftRunnableConsumerProducer = createLeftRunnableConsumerProducer

    protected val rightRunnableProducerConsumer: RightRunnableProducerConsumer = createRightRunnableProducerConsumer

    def doLeftConsume(lc: LC): Try[Boolean]

    def doRightConsume(rc: RC): Try[Boolean]

    override def getLeftConsumer: api.Consumer[LC] = ???

    override def getRightConsumer: api.Consumer[RC] = ???

    override def getLeftProducer: api.Producer[LP] = ???

    override def getRightProducer: api.Producer[RP] = ???

    override def registerLeft(consumer: api.Consumer[LC]): Try[Boolean] = ???

    override def registerRight(consumer: api.Consumer[RC]): Try[Boolean] = ???

    override def registerLeft(producer: api.Producer[LP]): Try[Boolean] = ???

    override def registerRight(producer: api.Producer[RP]): Try[Boolean] = ???
  }
}