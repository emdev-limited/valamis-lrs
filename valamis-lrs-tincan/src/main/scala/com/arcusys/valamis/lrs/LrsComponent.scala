package com.arcusys.valamis.lrs

import java.util.UUID

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
  * Lrs repository component - storage abstraction of LRS API
  */
trait LrsComponent {

  val statementStorage: StatementStorage
  val activityProfileStorage: ActivityProfileStorage
  val agentProfileStorage: AgentProfileStorage
  val stateProfileStorage: StateProfileStorage

  trait StatementStorage {
    def findStatement(id: Statement#Id): Option[Statement]

    def isVoidedStatement(id: Statement#Id): Boolean

    def findStatementsByParams(params: StatementQuery): PartialSeq[Statement]

    def containStatement(s: Statement): Boolean

    def addStatement(statement: Statement): UUID

    def getActivities(activity: String): Seq[Activity]

    def getActivity(activityId: Activity#Id): Option[Activity]

    def getPerson(agent: Agent): Person
  }

  trait DocumentStorage

  trait ActivityProfileStorage extends DocumentStorage{
    def add(activityProfile: ActivityProfile): Unit
    def update(activityProfile: ActivityProfile): Unit
    def findBy(activityId: Activity#Id, profileId: ProfileId): Option[Document]
    def findBy(activityId: Activity#Id, since: Option[DateTime]): Seq[ProfileId]
    def delete(activityId: Activity#Id, profileId: ProfileId): Unit
  }

  trait AgentProfileStorage extends DocumentStorage{
    def add(agentProfile: AgentProfile): Unit
    def update(agentProfile: AgentProfile): Unit
    def findBy(agent: Agent, profileId: ProfileId): Option[Document]
    def findBy(agent: Agent, since: Option[DateTime]): Seq[ProfileId]
    def delete(agent: Agent, profileId: ProfileId): Unit
  }

  trait StateProfileStorage extends DocumentStorage{
    def add(stateProfile: StateProfile): Unit
    def update(stateProfile: StateProfile): Unit
    def findBy(agent: Agent, activityId: String, stateId: String, registration: Option[UUID]): Option[Document]
    def findBy(agent: Agent, activityId:   Activity#Id, registration: Option[UUID], since: Option[DateTime]): Seq[ProfileId]
    def delete(agent: Agent, activityId: Activity#Id, stateId: String, registration: Option[UUID]): Unit
    def delete(agent: Agent, activityId: String, registration: Option[UUID]): Unit
  }

}