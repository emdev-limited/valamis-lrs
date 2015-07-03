package com.arcusys.valamis.lrs.services

import com.arcusys.valamis.lrs.converter.AccountConverter._
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.lrs.datasource.row.{AgentProfileRow, AgentRow, DocumentRow}
import com.arcusys.valamis.lrs.exception.RecordNotFoundException
import com.arcusys.valamis.lrs.tincan.{Agent, ContentType, Document, Person}
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.joda.time.DateTime

private[services] trait AgentComponent {
  this: DataContext with StatementSaverComponent  =>

  import driver.simple._
  import jodaSupport._

  /**
   * Return a special, Person Object for a specified Agent.
   * The Person Object is very similar to an Agent Object, but instead of each attribute having a single value,
   * each attribute has an array value, and it is legal to include multiple identifying properties.
   * Note that the parameter is still a normal Agent Object with a single identifier and no arrays.
   * Note that this is different from the FOAF concept of person, person is being used here to indicate
   * a person-centric view of the LRS Agent data, but Agents just refer to one persona (a person in one context).
   * @param agent The Agent associated with this profile.
   * @return Person Object
   */
  def getPerson(agent: Agent): Person = db.withSession(implicit session => {
    val filteredActors = actors
      .leftJoin(accounts).on { (actor, account) => actor.accountKey === account.key }
      .filter { x =>
        var expr = x._1.mBox === agent.mBox        ||
          x._1.mBoxSha1Sum   === agent.mBoxSha1Sum ||
          x._1.openId        === agent.openId

        agent.account.foreach { account => expr = expr ||
          x._2.name     === account.name &&
          x._2.homepage === account.homePage
        }
        expr
      } list

    Person(
      filteredActors collect { case x if x._1.name.isDefined        => x._1.name.get        },
      filteredActors collect { case x if x._1.mBox.isDefined        => x._1.mBox.get        },
      filteredActors collect { case x if x._1.mBoxSha1Sum.isDefined => x._1.mBoxSha1Sum.get },
      filteredActors collect { case x if x._1.openId.isDefined      => x._1.openId.get      },
      filteredActors map     { x => asModel(x._2) }
    )
  })

  /**
   * Loads ids of all profile entries for an Agent. If "since" parameter is specified,
   * this is limited to entries that have been stored or updated since the specified timestamp (exclusive).
   * @param agent The Agent associated with this profile.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return Lis of ids
   */
  def getProfiles(agent: Agent,
                  since: Option[DateTime] = None): Seq[String] = db.withSession { implicit session =>
    getAgentKey(agent) match {
      case Some(value) => getAgentDocuments(value, since) map { x => x.key }
      case None        => Seq()
    }
  }

  /**
   * Get the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def getProfileContent(agent:     Agent,
                        profileId: String): Option[Document] = db.withSession { implicit session =>
    getAgentKey(agent) match {
      case Some(value) => getAgentDocumentRow(value, profileId) map { x => x.toModel }
      case None => None
    }
  }

  /**
   * Delete the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def deleteProfile(agent:     Agent,
                    profileId: String): Unit = db.withSession { implicit session =>

    getAgentKey(agent) match {
      case Some(value) => getAgentDocumentRow(value, profileId) match {

        case None           => Unit

        case Some(document) =>
          documents filter { x =>
            x.key === document.key
          } delete match {

            case result if result > 0 => Unit

            case _                    =>
              throw new InternalError(s"Can not delete profile document: profileId = $profileId, agent = ${agent.toString} ")
          }
      }

      case None => Unit
    }
  }

  /**
   * Store the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   * @param doc The document of profile
   */
  def addOrUpdateDocument(agent:     Agent,
                          profileId: String,
                          doc:       Document): Unit = db.withSession { implicit session =>
    val agentKey = getStatementObjectKey(agent)

    getAgentDocumentRow(agentKey, profileId) match {
      case None =>
        documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
        agentProfiles.insert(AgentProfileRow(profileId, agentKey, doc.id.toString))

      case Some(document) =>
        val newContent = if (doc.cType == ContentType.json && document.cType == ContentType.json)
          JsonHelper.combine(document.contents, doc.contents)
        else
          doc.contents

        val newDoc = document.copy(
          contents = newContent,
          cType    = doc.cType,
          updated  = DateTime.now
        )

        documents filter { x => x.key === newDoc.key } update (newDoc)
    }
  }

  private def agentProfileQuery(implicit session: Session) =
      agentProfiles
      .join(documents).on((sp, document) => sp.documentKey === document.key)

  private def filterAgentProfileQuery(agentKey:  AgentRow#Type,
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

  private def getAgentDocumentQuery(agentKey:  AgentRow#Type,
                                    profileId: Option[String]   = None,
                                    since:     Option[DateTime] = None)
                                   (implicit session: Session) = filterAgentProfileQuery(agentKey, profileId, since).map(x => x._2)

  private def getAgentDocumentRow(agentKey:  AgentRow#Type,
                                  profileId: String)
                                 (implicit session: Session) = getAgentDocumentQuery(agentKey, Option(profileId)).firstOption

  private def getAgentDocuments(agentKey: AgentRow#Type,
                                since:    Option[DateTime] = None)
                               (implicit session: Session) =
    getAgentDocumentQuery(agentKey, None, since).list.distinct

}
