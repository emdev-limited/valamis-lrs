package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
object InteractionComponents {

  val empty = Map()
  val typical = Map("id" -> "test")
  val idOnly = Map("id" -> "test")
  val allProperties = Map(
    "id" -> "test",
    "description" -> Map("en-US" -> "test")
  )
}
