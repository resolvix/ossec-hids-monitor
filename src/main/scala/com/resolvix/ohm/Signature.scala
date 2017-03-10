package com.resolvix.ohm

class Signature(

  //
  //
  //
  private val id: Int,

  //
  //
  //
  private val ruleId: Int,

  //
  //
  //
  private val level: Int,

  //
  //
  //
  private val description: String

) extends api.Signature {

  //
  //
  //
  private var categories: List[Category] = _

  def getId: Int = id

  def getRuleId: Int = ruleId

  def getLevel: Int = level

  def getDescription: String = description

  def setCategories(
    categories: List[Category]
  ): Unit = {
    this.categories = categories.toList
  }
}
