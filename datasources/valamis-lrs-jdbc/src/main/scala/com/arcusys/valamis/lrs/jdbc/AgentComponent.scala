package com.arcusys.valamis.lrs.jdbc

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.schema.AgentProfileSchema
import org.joda.time.DateTime

@deprecated
trait AgentComponent {
  this: LrsDataContext
    with AgentProfileSchema
    with StatementApi
    with DocumentApi
    with ActorApi
    with StatementObjectApi  =>

  import driver.simple._
  import jodaSupport._

  def agentProfileQuery(implicit session: Session) =
    agentProfiles
      .join(documents).on((sp, document) => sp.documentKey === document.key)

  def filterAgentProfileQuery(agentKey:  AgentRow#Type,
                                      profileId: Option[String]   = None,
                                      since:     Option[DateTime] = None)
                                     (implicit session: Session) = {

    var query = agentProfileQuery filter { j => j._1.agentKey === agentKey }

    query = profileId match {
      case Some(value) => query filter { j => j._1.profileId === value }
      case None => query
    }

    query = since match {
      case Some(value) => query filter { j => j._2.updated >= value }
      case None => query
    }

    query
  }

  def getAgentDocumentQuery(agentKey:  AgentRow#Type,
                                    profileId: Option[String]   = None,
                                    since:     Option[DateTime] = None)
                                   (implicit session: Session) = filterAgentProfileQuery(agentKey, profileId, since).map(x => x._2)

  def getAgentDocumentRow(agentKey:  AgentRow#Type,
                                  profileId: String)
                                 (implicit session: Session) = getAgentDocumentQuery(agentKey, Option(profileId)).firstOption

  def getAgentDocuments(agentKey: AgentRow#Type,
                                since:    Option[DateTime] = None)
                               (implicit session: Session) =
    getAgentDocumentQuery(agentKey, None, since).list.distinct

}
