package com.resolvix.ohm

import com.resolvix.ohm.dao.TestOssecHidsDAO
import org.scalatest.FlatSpec

/**
  * Created by rwbisson on 10/10/16.
  */
class OssecHidMonitorTest
  extends FlatSpec
{

  "OssecHidsMonitor" should "accept a DAO to enable it to initialise itself" in {
    val testOssecHidsDAO: dao.api.OssecHidsDAO = new TestOssecHidsDAO
    val ossecHidsMonitor: OssecHidsMonitor = new OssecHidsMonitor(testOssecHidsDAO)
  }



}
