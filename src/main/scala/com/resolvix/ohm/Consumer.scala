package com.resolvix.ohm

import com.resolvix.concurrent.api.{Consumer, Producer}

/**
  * Created by rwbisson on 23/10/16.
  */
class Consumer[C]
  extends Consumer[Consumer[C], Producer[C], C]
{

}
