package com.arcusys.valamis.lrs.test.tincan

import com.arcusys.valamis.lrs._

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
case class Verb(id:      Option[String] = None,
                display: Option[Map[String, Any]] = None)

object Verbs {

  val invalidUri = "_abc://should.fail.com"
  val invalidUri2 = "Test"
  val invalidUri3 = "http://adlnet.gov/expapi/ verbs/experienced"
  val validUri   = "http://tincanapi.com/conformancetest/verbid"
  val voidedUri  = "http://adlnet.gov/expapi/verbs/voided/"

  object Good {
    val `should pass verb with id only`                  = minimal
    val `should pass verb with valid uri id and display` = typical
    val `should pass verb with voided uri id`            = voiding
  }

  object Bad {
    val empty              = Some(Verb())
    val displayOnly        = Some(Verb(None,         LanguageMaps.good3))
    val invalidUriId       = Some(Verb(invalidUri ?, LanguageMaps.good3))
    val invalidUriId2      = Some(Verb(invalidUri2 ?, LanguageMaps.good3))
    val invalidUriId3      = Some(Verb(invalidUri3 ?, LanguageMaps.good3))
    val invalidLanguageMap = Some(Verb(validUri   ?, LanguageMaps.bad))
  }

  val minimal      = Some(Verb(validUri  ?))
  val typical      = Some(Verb(validUri  ?, LanguageMaps.good3))
  val voiding      = Some(Verb(voidedUri ?, LanguageMaps.good3))
}
