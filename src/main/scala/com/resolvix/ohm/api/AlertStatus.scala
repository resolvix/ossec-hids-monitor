package com.resolvix.ohm.api

/**
  * Created by rwbisson on 08/10/16.
  */
trait AlertStatus {

  def getId: Int

  def getModuleId: Int

  def getReference: String

  def getStatusId: Int

}
