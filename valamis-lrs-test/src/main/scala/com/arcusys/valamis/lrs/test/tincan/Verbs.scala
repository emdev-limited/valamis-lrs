package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 12/02/15.
 */
case class Verb(id:      Option[String] = None,
                display: Option[Map[String, Any]] = None)

object Verbs {

  val invalidUri = Some("_abc://should.fail.com")
  val validUri   = Some("http://tincanapi.com/conformancetest/verbid")
  val voidedUri  = Some("http://adlnet.gov/expapi/verbs/voided/")

  object Good {
    val `should pass verb with id only`                  = minimal
    val `should pass verb with valid uri id and display` = typical
    val `should pass verb with voided uri id`            = voiding
  }

  object Bad {
    val empty              = Some(Verb())
    val displayOnly        = Some(Verb(None,       LanguageMaps.good3))
    val invalidUriId       = Some(Verb(invalidUri, LanguageMaps.good3))
    val invalidLanguageMap = Some(Verb(validUri,   LanguageMaps.bad))
  }

  val minimal      = Some(Verb(validUri))
  val typical      = Some(Verb(validUri,  LanguageMaps.good3))
  val voiding      = Some(Verb(voidedUri, LanguageMaps.good3))
}
