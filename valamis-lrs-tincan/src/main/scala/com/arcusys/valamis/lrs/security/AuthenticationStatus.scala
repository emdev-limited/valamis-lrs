package com.arcusys.valamis.lrs.security

/**
 * Created by Iliya Tryapitsin on 15/01/15.
 */
object AuthenticationStatus extends Enumeration {
  type Type = Value

  val Allowed = Value("Allowed")
  val Denied  = Value("Denied")
  val Forbidden = Value("Forbidden")

  def apply(boolean: Boolean) = if (boolean) Allowed
  else Denied
}
