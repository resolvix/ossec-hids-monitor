package com.resolvix.ohm

class Location(

  //
  //
  //
  private val id: Int,

  //
  //
  //
  private val serverId: Int,

  //
  //
  //
  private val name: String

) extends module.api.Location {

  def getId: Int = id

  def getServerId: Int = serverId

  def getName: String = name

}
