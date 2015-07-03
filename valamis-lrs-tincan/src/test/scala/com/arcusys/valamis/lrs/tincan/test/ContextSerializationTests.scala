package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer.ContextSerializer
import com.arcusys.valamis.lrs.test.tincan.Contexts
import com.arcusys.valamis.lrs.tincan.Context
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.slf4j.LoggerFactory

/**
 * Created by Iliya Tryapitsin on 11/03/15.
 */
class ContextSerializationTests extends BaseSerializationTests {
  behavior of "Context serializer/deserializer testing"

  val logger = LoggerFactory.getLogger(classOf[Context])

  it should "serialize/deserialize contexts" in {
    val typical = JsonHelper.toJson(Contexts.typical)
    val typicalContext = JsonHelper.fromJson[Context](typical, new ContextSerializer)
    val typicalContextSerialized = JsonHelper.toJson(typicalContext, new ContextSerializer)

    val typicalAgentInstructor = JsonHelper.toJson(Contexts.typicalAgentInstructor)
    val typicalAgentInstructorContext = JsonHelper.fromJson[Context](typicalAgentInstructor, new ContextSerializer)
    val typicalAgentInstructorContextSerialized = JsonHelper.toJson(typicalAgentInstructorContext, new ContextSerializer)

    val mboxAndTypeAgentInstructor = JsonHelper.toJson(Contexts.mboxAndTypeAgentInstructor)
    val mboxAndTypeAgentInstructorContext = JsonHelper.fromJson[Context](mboxAndTypeAgentInstructor, new ContextSerializer)
    val mboxAndTypeAgentInstructorContextSerialized = JsonHelper.toJson(mboxAndTypeAgentInstructorContext, new ContextSerializer)

    val mboxSha1AndTypeAgentInstructor = JsonHelper.toJson(Contexts.mboxSha1AndTypeAgentInstructor)
    val mboxSha1AndTypeAgentInstructorContext = JsonHelper.fromJson[Context](mboxSha1AndTypeAgentInstructor, new ContextSerializer)
    val mboxSha1AndTypeAgentInstructorContextSerialized = JsonHelper.toJson(mboxSha1AndTypeAgentInstructorContext, new ContextSerializer)

    val openidAndTypeAgentInstructor = JsonHelper.toJson(Contexts.openidAndTypeAgentInstructor)
    val openidAndTypeAgentInstructorContext = JsonHelper.fromJson[Context](openidAndTypeAgentInstructor, new ContextSerializer)
    val openidAndTypeAgentInstructorContextSerialized = JsonHelper.toJson(openidAndTypeAgentInstructorContext, new ContextSerializer)

    val accountAndTypeAgentInstructor = JsonHelper.toJson(Contexts.accountAndTypeAgentInstructor)
    val accountAndTypeAgentInstructorContext = JsonHelper.fromJson[Context](accountAndTypeAgentInstructor, new ContextSerializer)
    val accountAndTypeAgentInstructorContextSerialized = JsonHelper.toJson(accountAndTypeAgentInstructorContext, new ContextSerializer)

    val typicalGroupTeam = JsonHelper.toJson(Contexts.typicalGroupTeam)
    val typicalGroupTeamContext = JsonHelper.fromJson[Context](typicalGroupTeam, new ContextSerializer)
    val typicalGroupTeamContextSerialized = JsonHelper.toJson(typicalGroupTeamContext, new ContextSerializer)

    val statementOnly = JsonHelper.toJson(Contexts.statementOnly)
    val statementOnlyContext = JsonHelper.fromJson[Context](statementOnly, new ContextSerializer)
    val statementOnlyContextSerialized = JsonHelper.toJson(statementOnlyContext, new ContextSerializer)

    val extensionsOnly = JsonHelper.toJson(Contexts.extensionsOnly)
    val extensionsOnlyContext = JsonHelper.fromJson[Context](extensionsOnly, new ContextSerializer)
    val extensionsOnlyContextSerialized = JsonHelper.toJson(extensionsOnlyContext, new ContextSerializer)

    val emptyExtensionsOnly = JsonHelper.toJson(Contexts.emptyExtensionsOnly)
    val emptyExtensionsOnlyContext = JsonHelper.fromJson[Context](emptyExtensionsOnly, new ContextSerializer)
    val emptyExtensionsOnlyContextSerialized = JsonHelper.toJson(emptyExtensionsOnlyContext, new ContextSerializer)

    val emptyContextActivities = JsonHelper.toJson(Contexts.emptyContextActivities)
    val emptyContextActivitiesContext = JsonHelper.fromJson[Context](emptyContextActivities, new ContextSerializer)
    val emptyContextActivitiesContextSerialized = JsonHelper.toJson(emptyContextActivitiesContext, new ContextSerializer)

    val emptyContextActivitiesAllPropertiesEmpty = JsonHelper.toJson(Contexts.emptyContextActivitiesAllPropertiesEmpty)
    val emptyContextActivitiesAllPropertiesEmptyContext = JsonHelper.fromJson[Context](emptyContextActivitiesAllPropertiesEmpty, new ContextSerializer)
    val emptyContextActivitiesAllPropertiesEmptyContextSerialized = JsonHelper.toJson(emptyContextActivitiesAllPropertiesEmptyContext, new ContextSerializer)

    val contextActivitiesAllPropertiesOnly = JsonHelper.toJson(Contexts.contextActivitiesAllPropertiesOnly)
    val contextActivitiesAllPropertiesOnlyContext = JsonHelper.fromJson[Context](contextActivitiesAllPropertiesOnly, new ContextSerializer)
    val contextActivitiesAllPropertiesOnlyContextSerialized = JsonHelper.toJson(contextActivitiesAllPropertiesOnlyContext, new ContextSerializer)

    val allProperties = JsonHelper.toJson(Contexts.allProperties)
    val allPropertiesContext = JsonHelper.fromJson[Context](allProperties, new ContextSerializer)
    val allPropertiesContextSerialized = JsonHelper.toJson(allPropertiesContext, new ContextSerializer)
  }
}
