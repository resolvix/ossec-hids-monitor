package com.resolvix.ohm.api
import com.resolvix.concurrent.api.Configuration

import scala.util.Try

/**
  * Created by rwbisson on 23/10/16.
  */
abstract class Consumer[C]
  extends com.resolvix.concurrent.Consumer[Consumer[C], Producer[C], C]
{
  /**
    *
    * @param configuration
    * @return
    */
  override def initialise(
    configuration: Configuration
  ): Try[Boolean] = {
    super.initialise(configuration)
  }
}
