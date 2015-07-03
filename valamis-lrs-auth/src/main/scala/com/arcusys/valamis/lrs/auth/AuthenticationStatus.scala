package com.arcusys.valamis.lrs.auth

/**
 * Created by Iliya Tryapitsin on 15/01/15.
 */
object AuthenticationStatus extends Enumeration {
  type Type = Value

  val Allowed = Value("Allowed")
  val Denied  = Value("Denied")

  def apply(boolean: Boolean) = if (boolean) Allowed
  else Denied
}
