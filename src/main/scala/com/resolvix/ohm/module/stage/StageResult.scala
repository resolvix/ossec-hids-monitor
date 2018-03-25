package com.resolvix.ohm.module.stage

class StageResult[O](
  //
  //
  //
  outputs: Array[O]
) extends com.resolvix.ohm.module.stage.api.StageResult[O] {
  override def getOutputs: Iterable[O] = {
    outputs.toIterable
  }
}
