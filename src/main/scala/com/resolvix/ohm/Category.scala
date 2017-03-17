package com.resolvix.ohm

class Category(

  //
  //
  //
  private val id: Int,

  //
  //
  //
  private val name: String

) extends module.api.Category {

  //
  //
  //
  private var signatures: List[Signature] = _

  def getId: Int = id

  def getName: String = name

  def setSignatures(
    signatures: List[Signature]
  ): Unit = {
    this.signatures = signatures.toList
  }
}
