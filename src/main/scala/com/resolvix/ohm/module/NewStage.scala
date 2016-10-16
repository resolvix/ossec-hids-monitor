package com.resolvix.ohm.module

import com.resolvix.ohm.api.{Alert, ConsumerProducerModule}

import scala.util.Try

/**
  * Created by rwbisson on 16/10/16.
  */
class NewStage
  extends ConsumerProducerModule[Alert, AugmentedAlert]
{
  override def convert(alert: Alert): AugmentedAlert = {
    new AugmentedAlert(alert)
  }
}
