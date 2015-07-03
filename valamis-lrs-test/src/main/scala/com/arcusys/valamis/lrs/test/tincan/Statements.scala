package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class Statement(actor:     Option[Actor] = None,
                     verb:      Option[Verb] = None,
                     `object`:  Option[StatementObj] = None,
                     id:        Option[String] = None,
                     timestamp: Option[String] = None,
                     authority: Option[Actor] = None,
                     context:   Option[Context] = None,
                     attachments: Option[Seq[StatementMetadata]] = None,
                     result:    Option[Result] = None,
                     stored:    Option[String] = None,
                     version:   Option[String] = None)

object Statements {

  val invalidDate     = "01/011/2015"
  val invalidNumeric  = "12345"
  val invalidObject   = "{key: 'should fail'}"
  val invalidString   = "should fail"
  val invalidVersion099  = "0.9.9"
  val invalidVersion110  = "1.1.0"
  val invalidUuidManyDigits     = "AA97B177-9383-4934-8543-00F91A7A028368"
  val invalidUuidInvalidLetter  = "MA97B177-9383-4934-8543-0F91A7A02836"

  val validPeriod10   = "1.0"
  val validPeriod101  = "1.0.1"

  object Bad {
    import Good._

    val `statement "object" missing reply 400` = minimal.copy(`object` = None)
    val `statement "verb"   missing reply 400` = minimal.copy( verb    = None)
    val `statement "actor"  missing reply 400` = minimal.copy( actor   = None)

    val `statement object  should fail on "null"`  = minimal.copy(`object` = null)
    val `statement context should fail on "null"`  = minimal.copy( context = null)
    val `statement verb    should fail on "null"`  = minimal.copy( verb    = null)
    val `statement actor   should fail on "null"`  = minimal.copy( actor   = null)

    val `statement verb should fail on empty` = minimal.copy(verb = Verbs.Bad.empty)
    val `statement verb missing "id"`         = minimal.copy(verb = Verbs.Bad.displayOnly)
    val `statement verb "id" not IRI`         = minimal.copy(verb = Verbs.Bad.invalidUriId)
    val `statement verb voided does not use object "StatementRef"` = minimal.copy(verb = Verbs.voiding)
    val `statement verb "display" not language` = minimal.copy(verb = Verbs.Bad.invalidLanguageMap)

    val `statement "id" invalid numeric`                   = minimal.copy(id = Some(invalidNumeric))
    val `statement "id" invalid object`                    = minimal.copy(id = Some(invalidObject))
    val `statement "id" invalid UUID with too many digits` = minimal.copy(id = Some(invalidUuidManyDigits))
    val `statement "id" invalid UUID with non A-F`         = minimal.copy(id = Some(invalidUuidInvalidLetter))

    val `statement "timestamp" invalid date`   = minimal.copy(timestamp = Some(invalidDate))
    val `statement "timestamp" invalid string` = minimal.copy(timestamp = Some(invalidString))

    val `statement "stored" invalid date`   = minimal.copy(stored = Some(invalidDate))
    val `statement "stored" invalid string` = minimal.copy(stored = Some(invalidString))

    val `statement "version" invalid 0.9.9` = minimal.copy(version = Some(invalidVersion099))
    val `statement "version" invalid 1.1.0` = minimal.copy(version = Some(invalidVersion110))
    val `statement "version" invalid string`= minimal.copy(version = Some(invalidString))

    val `statement sub-statement verb missing "id"` = minimal.copy(`object` = SubStatements.Bad.verbDisplayOnly)
    val `statement sub-statement verb "id" not IRI` = minimal.copy(`object` = SubStatements.Bad.verbNotUriId)
  }

  object Good {
    def minimal = Statement(actor = Agents.typical, verb = Verbs.typical, `object` = Activities.typical)
    def minimalSubStmnt = minimal.copy(`object` = SubStatements.typical)
    def voiding(ref: StatementRef) = minimal.copy(verb = Verbs.voiding, id = UUIDs.unique, `object` = Some(ref))
    def voiding(ref: Option[StatementRef]) = minimal.copy(verb = Verbs.voiding, id = UUIDs.unique, `object` = ref)

    val `should pass statement minimal`           = minimal
    val `should pass statement typical`           = minimal.copy(id          = UUIDs.unique,               timestamp = Timestamps.good)
    val `should pass statement authority typical` = minimal.copy(authority   = Agents.typical)
    val `should pass statement attachments`       = minimal.copy(attachments = Attachments.oneTextMetadata)
    val `should pass sub-statement verb typical`  = minimalSubStmnt

    val `statement activity extensions can be empty` = minimal.copy(`object` = Activities.noExtensions)
    val `statement activity extensions can be non empty` = minimal.copy(`object` = Activities.allProperties)
    val `statement result   extensions can be empty` = minimal.copy(result   = Results.noExtensions)
    val `statement context  extensions can be empty` = minimal.copy(context  = Contexts.noExtensions)
    val `statement context  extensions can be non empty` = minimal.copy(context  = Contexts.allProperties)

    val `statement sub-statement activity extensions can be empty` = minimalSubStmnt.copy(`object` = SubStatements.activityWithNoExtensions)
    val `statement sub-statement result   extensions can be empty` = minimalSubStmnt.copy( result  = Results.noExtensions)
    val `statement sub-statement context  extensions can be empty` = minimalSubStmnt.copy( context = Contexts.noExtensions)

    val `statement "version" valid 1.0`   = minimal.copy(version = Some(validPeriod10))
    val `statement "version" valid 1.0.9` = minimal.copy(version = Some(validPeriod101))

    val `statement verb voided IRI ends with "voided"` = voiding(StatementRefs.typical)

  }
}
