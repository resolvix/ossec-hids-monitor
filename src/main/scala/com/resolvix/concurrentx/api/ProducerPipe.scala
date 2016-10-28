package com.resolvix.concurrentx.api

import scala.util.Try

trait ProducerPipe[T]
  extends Pipe[T]
{

  /**
    *
    * @param t
    * @return
    */
  def write(
    t: T
  ): Try[Boolean]
}
