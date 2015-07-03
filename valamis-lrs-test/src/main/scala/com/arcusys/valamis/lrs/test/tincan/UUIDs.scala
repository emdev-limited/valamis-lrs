package com.arcusys.valamis.lrs.test.tincan

import java.util.UUID

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
object UUIDs {
  val good = Some("39e24cc4-69af-4b01-a824-1fdc6ea8a3af")
  val bad = Some("bad-uuid")
  def unique = Some(UUID.randomUUID().toString)
}
