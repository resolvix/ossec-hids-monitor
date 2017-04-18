package com.resolvix.ohm.module.stage.api

trait StageResult[O]
  extends com.resolvix.ohm.module.api.Result
{
  def getOutputs: Iterable[O]
}
