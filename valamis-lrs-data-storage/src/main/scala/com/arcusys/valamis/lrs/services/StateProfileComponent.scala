package com.arcusys.valamis.lrs.services

import com.arcusys.valamis.lrs.datasource.row.AgentRow
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.utils.serialization.JsonHelper
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
import java.util.UUID

private[services] trait StateProfileComponent {
  this: DataContext with StatementSaverComponent =>

  import driver.simple._
  import jodaSupport._

  def getProfile(agent: Agent,
                 activityId: String,
                 stateId: String,
                 registration: Option[UUID]): Option[Document] =
    db.withSession { implicit session =>
      getAgentKey(agent) match {
        case Some(value) => getDocumentRow(value, activityId, stateId, registration).map(x => x.toModel)
        case None => None
      }
    }

  def getProfiles(agent: Agent,
                  activityId: String,
                  registration: Option[UUID],
                  since: Option[DateTime]): Seq[String] = db.withSession { implicit session =>
    getAgentKey(agent) match {
      case Some(value) => getDocuments(value, activityId, registration, since).map(x => x.key)
      case None => Seq()
    }
  }

  def deleteProfiles(agent: Agent,
                     activityId: String,
                     registration: Option[UUID]): Unit = db.withSession { implicit session =>
    getAgentKey(agent) match {
      case Some(value) =>
        val docKeys = getDocuments(value, activityId, registration, None) map { x => x.key }
        documents filter { x => x.key inSet docKeys } delete

      case None => Unit
    }
  }

  /**
   * Add or Update if exist the specified profile document in the context of the specified Activity.
   * @param agent The agent associated with these profiles.
   * @param activityId The activity id associated with these profiles.
   * @param stateId The state id associated with this profile.
   * @param registration The registration UUID associated with this profile.
   * @param doc The new document version
   */
  def addOrUpdateDocument(agent: Agent,
                          activityId: String,
                          stateId: String,
                          registration: Option[UUID],
                          doc: Document): Unit = db.withSession(implicit session => {
    val agentKey = getStatementObjectKey(agent)

    getDocumentRow(agentKey, activityId, stateId, registration) match {
      case None =>
        import com.arcusys.valamis.lrs.datasource.row.{StateProfileRow, DocumentRow}
        val activityKey = getStatementObjectKey(Activity(id = activityId))

        documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
        stateProfiles.insert(StateProfileRow(stateId, agentKey, activityKey, registration.map(_.toString), doc.id.toString))

      case Some(document) =>
        val newContent = if (doc.cType == ContentType.json && document.cType == ContentType.json)
          JsonHelper.combine(document.contents, doc.contents)
        else
          doc.contents

        val newDoc = document.copy(
          contents = newContent,
          cType = doc.cType,
          updated = DateTime.now)

        documents.filter(x => x.key === newDoc.key).update(newDoc)
    }
  })

  def deleteProfile(agent: Agent,
                    activityId: String,
                    stateId: String,
                    registration: Option[UUID]): Unit = db.withSession(implicit session => {
    getAgentKey(agent) match {
      case Some(value) => getDocumentRow(value, activityId, stateId, registration) match {
        case None    => Unit
        case Some(d) => documents filter { x => x.key === d.key } delete
      }
      case None => Unit
    }
  })


  private def stateProfileQuery(implicit session: Session) =
      stateProfiles
      .join(activities).on((sp, a) => sp.activityKey === a.key)
      .join(documents).on((sp, document) => sp._1.documentKey === document.key)

  private def filterStateProfileQuery(agentKey: AgentRow#Type,
                                      activityId: String,
                                      stateId: Option[String],
                                      registration: Option[UUID],
                                      since: Option[DateTime] = None)
                                     (implicit session: Session) = {

    var query = stateProfileQuery.filter(j => j._1._2.id === activityId && j._1._1.agentKey === agentKey)

    query = registration.map(_.toString) match {
      case None => query
        .filter(j => j._1._1.registration.isEmpty)
      case Some(reg) => query
        .filter(j => j._1._1.registration.isDefined && j._1._1.registration === reg)
    }
    query = stateId match {
      case Some(value) => query.filter(j => j._1._1.stateId === value)
      case None => query
    }
    query = since match {
      case Some(value) => query.filter(j => j._2.updated >= value)
      case None => query
    }
    query
  }

  private def getDocumentQuery(agentKey: AgentRow#Type,
                               activityId: String,
                               stateId: Option[String],
                               registration: Option[UUID],
                               since: Option[DateTime] = None)
                              (implicit session: Session) =
    filterStateProfileQuery(agentKey, activityId, stateId, registration, since).map(x => x._2)

  private def getDocumentRow(agentKey: AgentRow#Type,
                             activityId: String,
                             stateId: String,
                             registration: Option[UUID])
                            (implicit session: Session) =
    getDocumentQuery(agentKey, activityId, Option(stateId), registration).firstOption

  private def getDocuments(agentKey: AgentRow#Type,
                           activityId: String,
                           registration: Option[UUID],
                           since: Option[DateTime])
                          (implicit session: Session) =
    getDocumentQuery(agentKey, activityId, None, registration, since).list.distinct

}
