package com.resolvix.ohm

import com.resolvix.concurrent.Producer

/**
  * Created by rwbisson on 23/10/16.
  */
class Producer[P]
  extends Producer[[Producer[P], Consumer[P]], P]
{

}
