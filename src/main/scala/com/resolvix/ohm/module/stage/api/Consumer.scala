package com.resolvix.ohm.module.stage.api

/**
  *
  *
  */
trait Consumer[I] {

  def consume(input: I): Boolean

}
