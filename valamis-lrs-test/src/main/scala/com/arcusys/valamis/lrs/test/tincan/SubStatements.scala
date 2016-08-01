package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */
case class SubStatement(objectType: Option[String]       = None,
                        actor:      Option[Actor]        = None,
                        verb:       Option[Verb]         = None,
                        `object`:   Option[StatementObj] = None) extends StatementObj

object SubStatements {
  val objectType = Some("SubStatement")

  private[SubStatements] def minimal = SubStatement(objectType, Agents.typical, Verbs.typical, Activities.typical)
  val typical = Some(minimal)
  val activityWithNoExtensions = typical.map { x => x.copy(`object` = Activities.noExtensions ) }

  object Good {

    val `should pass sub-statement with agent actor with mbox`                     = minimal.copy(actor = Agents.mboxOnly           )
    val `should pass sub-statement with agent actor with mbox & type`              = minimal.copy(actor = Agents.mboxAndType        )
    val `should pass sub-statement with group actor with mbox & type`              = minimal.copy(actor = Groups.mboxAndType        )
    val `should pass sub-statement with agent actor with mbox sha1 & type`         = minimal.copy(actor = Agents.mboxSha1AndType    )
    val `should pass sub-statement with agent actor with mbox sha1`                = minimal.copy(actor = Agents.mboxSha1Only       )
    val `should pass sub-statement with agent actor with open id`                  = minimal.copy(actor = Agents.openidOnly         )
    val `should pass sub-statement with agent actor with open id & type`           = minimal.copy(actor = Agents.openidAndType      )
    val `should pass sub-statement with group actor with open id & type`           = minimal.copy(actor = Groups.openidAndType      )
    val `should pass sub-statement with group actor with mbox sha1, type & member` = minimal.copy(actor = Groups.mboxSha1TypeAndMember)
    val `should pass sub-statement with agent actor with account`                  = minimal.copy(actor = Agents.accountOnly        )
    val `should pass sub-statement with agent actor with account & type`           = minimal.copy(actor = Agents.accountAndType     )
    val `should pass sub-statement with group actor with account & type`           = minimal.copy(actor = Groups.accountAndType     )
    val `should pass sub-statement with agent actor with minimal verb`             = minimal.copy(verb  = Verbs.minimal             )
    val `should pass sub-statement with activities with all props`                 = minimal.copy(`object` = Activities.allProperties)
    val `should pass sub-statement with statement refs`                            = minimal.copy(`object` = StatementRefs.typical)
    val `should pass sub-statement with activities with no ext`                    = activityWithNoExtensions
    val `should pass sub-statement with object agent mbox`             = SubStatement(objectType, Agents.typical, verb = Verbs.typical, `object` = Agents.mboxAndType)
    val `should pass sub-statement with object agent mbox & type`      = minimal.copy(`object` = Agents.mboxAndType     )
    val `should pass sub-statement with object agent mbox sha1 & type` = minimal.copy(`object` = Agents.mboxSha1AndType )
    val `should pass sub-statement with object agent mbox sha1`        = minimal.copy(`object` = Agents.mboxSha1AndType    )
    val `should pass sub-statement with object agent openid & type`    = minimal.copy(`object` = Agents.openidAndType   )
    val `should pass sub-statement with object agent account only`     = minimal.copy(`object` = Agents.accountAndType     )
    val `should pass sub-statement with object agent account & type`   = minimal.copy(`object` = Agents.accountAndType  )
    val `should pass sub-statement with object group openid & type`    = minimal.copy(`object` = Groups.openidAndType   )
    val `should pass sub-statement with object group mbox & type`      = minimal.copy(`object` = Groups.mboxAndType     )
    val `should pass sub-statement with object group mbox sha1 & type` = minimal.copy(`object` = Groups.mboxSha1AndType )
    val `should pass sub-statement with object group account & type`   = minimal.copy(`object` = Groups.accountAndType  )
    val `should pass sub-statement with object group all prop`         = minimal.copy(`object` = Groups.allPropertiesTypicalAgentMember)
  }

  object Bad {
    val empty           = Some(SubStatement())
    val verbDisplayOnly = Some(minimal.copy(verb = Verbs.Bad.displayOnly))
    val verbNotUriId    = Some(minimal.copy(verb = Verbs.Bad.invalidUriId))
    val verbNotUriId2    = Some(minimal.copy(verb = Verbs.Bad.invalidUriId2))
    val verbNotUriId3    = Some(minimal.copy(verb = Verbs.Bad.invalidUriId3))

  }
}
