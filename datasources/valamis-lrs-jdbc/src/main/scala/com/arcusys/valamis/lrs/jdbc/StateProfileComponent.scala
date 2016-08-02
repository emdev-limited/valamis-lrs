package com.arcusys.valamis.lrs.jdbc

import java.util.UUID
import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

@deprecated
trait StateProfileComponent {
  this: LrsDataContext
    with StatementApi
    with ActorApi
    with StatementObjectApi =>

  import driver.simple._
  import jodaSupport._


  def stateProfileQuery(implicit session: Session) =
      stateProfiles
      .join(activities).on((sp, a) => sp.activityKey === a.key)
      .join(documents).on((sp, document) => sp._1.documentKey === document.key)

  def filterStateProfileQuery(agentKey: AgentRow#Type,
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

  def getDocumentQuery(agentKey: AgentRow#Type,
                               activityId: String,
                               stateId: Option[String],
                               registration: Option[UUID],
                               since: Option[DateTime] = None)
                              (implicit session: Session) =
    filterStateProfileQuery(agentKey, activityId, stateId, registration, since).map(x => x._2)

  def getDocumentRow(agentKey: AgentRow#Type,
                             activityId: String,
                             stateId: String,
                             registration: Option[UUID])
                            (implicit session: Session) =
    getDocumentQuery(agentKey, activityId, Option(stateId), registration).firstOption

  def getDocuments(agentKey: AgentRow#Type,
                           activityId: String,
                           registration: Option[UUID],
                           since: Option[DateTime])
                          (implicit session: Session) =
    getDocumentQuery(agentKey, activityId, None, registration, since).list.distinct

}
