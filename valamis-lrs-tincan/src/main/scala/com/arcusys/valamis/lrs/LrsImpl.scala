package com.arcusys.valamis.lrs

import java.util.UUID

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.exception.ConflictEntityException
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
  * LRS
  */
trait LrsImpl extends Lrs {
  this: LrsComponent =>

  // Statement API
  override def findStatements(query: StatementQuery): PartialSeq[Statement] = query match {
    case q if q.statementId isDefined       =>
                                                (if(!statementStorage.isVoidedStatement(q.statementId))
                                                  statementStorage findStatement q.statementId
                                                else
                                                  None) toPartialSeq

    case q if q.voidedStatementId isDefined =>
                                                (if(statementStorage.isVoidedStatement(q.voidedStatementId))
                                                  statementStorage findStatement q.voidedStatementId
                                                else
                                                  None) toPartialSeq

    case q                                  =>  statementStorage findStatementsByParams q

  }

  override def addStatement(statement: Statement): UUID = {
    if (statementStorage containStatement statement)
      throw new ConflictEntityException(s"Statement with key = '${statement.id}' already exist")
    statementStorage addStatement statement
  }

  //Document API
  //ActivityProfile API
  override def addOrUpdateActivityProfile(activityId: Activity#Id, profileId: ProfileId, doc: Document): Unit = {
    val actProfile = getActivityProfile(activityId, profileId)
    actProfile match {
      case None => activityProfileStorage.add(ActivityProfile(activityId, profileId, doc))
      case Some(oldDoc) =>
        val newContent = if (doc.cType == ContentType.json && oldDoc.cType == ContentType.json)
          JsonHelper.combine(oldDoc.contents, doc.contents)
        else
          doc.contents

        val combDoc = oldDoc.copy(
          contents = newContent,
          cType    = doc.cType,
          updated  = DateTime.now
        )
        activityProfileStorage.update(ActivityProfile(activityId, profileId, combDoc))
    }
  }
  override def getActivityProfile(activityId: Activity#Id, profileId: ProfileId): Option[Document] = activityProfileStorage.findBy(activityId, profileId)
  override def getActivityProfileIds(activityId: Activity#Id, since: Option[DateTime]): Seq[ProfileId] = activityProfileStorage.findBy(activityId, since)
  override def deleteActivityProfile(activityId: Activity#Id, profileId: ProfileId): Unit = activityProfileStorage.delete(activityId, profileId)

  //AgentProfile API
  override def addOrUpdateAgentProfile(agent: Agent, profileId: ProfileId, doc: Document): Unit = {
    val agProfile = getAgentProfile(agent, profileId)
    agProfile match {
      case None => agentProfileStorage.add(AgentProfile(profileId, agent, doc))
      case Some(oldDoc) =>
        val newContent = if (doc.cType == ContentType.json && oldDoc.cType == ContentType.json)
          JsonHelper.combine(oldDoc.contents, doc.contents)
        else
          doc.contents

        val combDoc = oldDoc.copy(
          contents = newContent,
          cType    = doc.cType,
          updated  = DateTime.now
        )
        agentProfileStorage.update(AgentProfile(profileId, agent, combDoc))
    }
  }

  override def getAgentProfile(agent: Agent, profileId: ProfileId): Option[Document] = agentProfileStorage.findBy(agent, profileId)
  override def getAgentProfiles(agent: Agent, since: Option[DateTime]): Seq[ProfileId] = agentProfileStorage.findBy(agent, since)
  override def deleteAgentProfile(agent: Agent, profileId: ProfileId): Unit = agentProfileStorage.delete(agent, profileId)

  //StateProfile API
  override def addOrUpdateStateProfile(agent: Agent, activityId: Activity#Id, stateId: String, registration: Option[UUID], doc: Document): Unit = {
    val agProfile = getStateProfile(agent, activityId, stateId, registration)
    agProfile match {
      case None => stateProfileStorage.add(StateProfile(agent, activityId, stateId, registration, doc))
      case Some(oldDoc) =>
        val newContent = if (doc.cType == ContentType.json && oldDoc.cType == ContentType.json)
          JsonHelper.combine(oldDoc.contents, doc.contents)
        else
          doc.contents

        val combDoc = oldDoc.copy(
          contents = newContent,
          cType    = doc.cType,
          updated  = DateTime.now
        )
        stateProfileStorage.update(StateProfile(agent, activityId, stateId, registration, combDoc))
    }
  }
  override def getStateProfile(agent: Agent, activityId: String, stateId: String, registration: Option[UUID]): Option[Document] =
    stateProfileStorage.findBy(agent, activityId, stateId,registration)
  override def deleteStateProfile(agent: Agent, activityId: Activity#Id, stateId: String, registration: Option[UUID]): Unit =
    stateProfileStorage.delete(agent,activityId,stateId,registration)

  override def getStateProfiles(agent: Agent, activityId: Activity#Id, registration: Option[UUID], since: Option[DateTime]): Seq[String] =
    stateProfileStorage.findBy(agent, activityId,registration,since)

  override def deleteStateProfiles(agent: Agent, activityId: Activity#Id, registration: Option[UUID]): Unit = stateProfileStorage.delete(agent, activityId, registration)



  //Activity API
  override def getActivities(activity: String): Seq[Activity] = statementStorage.getActivities(activity)
  override def getActivity(activityId: Activity#Id): Option[Activity] = statementStorage.getActivity(activityId)

  //Person API
  override def getPerson(agent: Agent): Person = statementStorage.getPerson(agent)

  implicit class DocumentExtensions(doc1: Document) {

    /**
     * Merge current document with new
     * @param doc2 Document
     * @return Updated copy of current document
     */
    def mergeWith(doc2: Document): Document = {
      val content = if (doc1.cType == ContentType.json && doc2.cType == ContentType.json)
        JsonHelper.combine(doc1.contents, doc2.contents)
      else
        doc2.contents

      doc1.copy(
        updated  = DateTime.now(),
        contents = content,
        cType    = doc2.cType
      )
    }
  }
}