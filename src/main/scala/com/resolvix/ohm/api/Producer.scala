package com.resolvix.ohm.api
import com.resolvix.concurrent.api.Configuration

import scala.util.Try

/**
  * Created by rwbisson on 23/10/16.
  */
abstract class Producer[P]
  extends com.resolvix.concurrent.Producer[Producer[P], Consumer[P], P]
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
