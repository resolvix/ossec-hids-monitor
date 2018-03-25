package com.resolvix.ohm.module.stage.api

trait StageResult[O]
  extends com.resolvix.ohm.module.api.Result
{
  /**
    * Returns the output values constituting the results of the stage.
    *
    * @return an iterable list of output values of type `O`
    */
  def getOutputs: Iterable[O]
}
