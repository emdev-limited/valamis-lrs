package com.arcusys.valamis.lrs.utils

/**
  * Created by iliyatryapitsin on 12/11/15.
  */
object RunningMode extends Enumeration {

  type Type = Value

  val Development = Value("Development")
  val Production  = Value("Production" )

  val AvailableModes = ValueSet(
    Development,
    Production
  )

  val Default = Development

  var current = Default

  def setCurrent(mode: String) = current = withName(mode)
  def setCurrent(mode: Type) = current = mode
}