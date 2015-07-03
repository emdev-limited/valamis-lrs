package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class Context(registration: Option[String] = None,
                   instructor: Option[Agent] = None,
                   team: Option[Group] = None,
                   statement: Option[StatementRef] = None,
                   extensions: Option[Extensions.Extension] = None,
                   contextActivities: Option[ContextActivity] = None,
                   revision: Option[String] = None,
                   platform: Option[String] = None,
                   language: Option[String] = None)

object Contexts {

  object Good {
    val `should pass empty context` = Context()
    val `should pass context with instructor is typical agent`     = Context(instructor = Agents.typical)
    val `should pass context with instructor is mbox & type`       = Context(instructor = Agents.mboxAndType)
    val `should pass context with instructor is mbox sha1 & type`  = Context(instructor = Agents.mboxSha1AndType)
    val `should pass context with instructor is openid & type`     = Context(instructor = Agents.openidAndType)
    val `should pass context with instructor is account & type`    = Context(instructor = Agents.accountAndType)
    val `should pass context with team is typical group`           = Context(team = Groups.typical)
    val `should pass context with stmnt is stmnt ref`              = Context(statement = StatementRefs.typical)
    val `should pass context with typical extensions`              = Context(extensions = Extensions.typical)
    val `should pass context with empty extensions`                = Context(extensions = Extensions.empty)
    val `should pass context with empty context activities`        = Context(contextActivities = ContextActivities.empty)
  }

  val empty = Some(Context())
  val typical = Some(Context())
  val typicalAgentInstructor = Some(Context(instructor = Agents.typical))
  val mboxAndTypeAgentInstructor = Some(Context(instructor = Agents.mboxAndType))
  val mboxSha1AndTypeAgentInstructor = Some(Context(instructor = Agents.mboxSha1AndType))
  val openidAndTypeAgentInstructor = Some(Context(instructor = Agents.openidAndType))
  val accountAndTypeAgentInstructor = Some(Context(instructor = Agents.accountAndType))
  val typicalGroupTeam = Some(Context(team = Groups.typical))
  val statementOnly = Some(Context(statement = StatementRefs.typical))
  val extensionsOnly = Some(Context(extensions = Extensions.typical))
  val emptyExtensionsOnly = Some(Context(extensions = Extensions.empty))
  val emptyContextActivities = Some(Context(contextActivities = ContextActivities.empty))
  val emptyContextActivitiesAllPropertiesEmpty = Some(Context(contextActivities = ContextActivities.allPropertiesEmpty))
  val contextActivitiesAllPropertiesOnly = Some(Context(contextActivities = ContextActivities.allProperties))
  val allProperties = Some(Context(registration = Some("16fd2706-8baf-433b-82eb-8c7fada847da"),
    instructor = Agents.typical,
    team = Groups.typical,
    contextActivities = ContextActivities.allProperties,
    revision = Some("test"),
    platform = Some("test"),
    language = Some("en-US"),
    statement = StatementRefs.allProperties,
    extensions = Extensions.typical))
  val noExtensions = Some(allProperties.get.copy(extensions = None))
}
