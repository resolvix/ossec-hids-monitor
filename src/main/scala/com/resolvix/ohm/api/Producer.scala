package com.resolvix.ohm.api

import com.resolvix.concurrent.api.{Configuration, ConsumerPipe, ProducerPipe}

import scala.util.Try

/**
  * Created by rwbisson on 23/10/16.
  */
abstract class Producer[P]
  extends com.resolvix.concurrent.Producer[Producer[P], Consumer[P], P]
{

}
