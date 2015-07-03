package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by iliyatryapitsin on 07.04.15.
 */
object LanguageMaps {
  val good1 = Some(Map("en"        -> "Bla-bla-bla"))
  val good2 = Some(Map("en-US"     -> "Bla-bla-bla"))
  val good3 = Some(Map("en-US-xxx" -> "Bla-bla-bla"))
  val bad   = Some(Map("a12345" -> "attended"))
}
