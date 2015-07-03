package com.arcusys.valamis.lrs.auth

/**
 * Created by Iliya Tryapitsin on 27.04.15.
 */
object AuthenticationType extends Enumeration {
  type Type = Value

  val OAuth = Value("oauth")
  val Basic = Value("basic")
}
