package com.arcusys.valamis.lrs

/**
 * Created by Iliya Tryapitsin on 10.08.15.
 */
object LrsType extends Enumeration {
  type Type = Value

  val SimpleName   = "Simple"
  val ExtendedName = "Extended"

  val Simple   = Value(SimpleName)
  val Extended = Value(ExtendedName)
}
