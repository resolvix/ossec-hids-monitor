package com.resolvix.ohm.module.stage

/**
  * Created by rwbisson on 18/04/17.
  */
class StageResult[O](
  //
  //
  //
  outputs: Array[O]

  //
  //
  //
) extends com.resolvix.ohm.module.stage.api.StageResult[O] {

  /**
    *
    * @return
    */
  override def getOutputs: Iterable[O] = {
    outputs.toIterable
  }
}
