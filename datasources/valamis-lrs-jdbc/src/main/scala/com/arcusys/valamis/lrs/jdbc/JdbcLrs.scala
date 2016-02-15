package com.arcusys.valamis.lrs.jdbc

import java.util.UUID

import com.arcusys.valamis.lrs.PartialSeq
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs._
import com.google.inject.{Singleton, Inject}
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * @author Created by Iliya Tryapitsin on 28/01/15.
 */
trait JdbcLrs
  extends LrsDataContext
  with Lrs
  with ActivityProfileApi
  with StatementApi
  with AgentComponent
  with ActivityProfileComponent
  with StateProfileComponent
  with AgentProfileApi
  with DocumentApi
  with ResultApi
  with ScoreApi
  with StatementObjectApi
  with AccountApi
  with ContextApi
  with SubStatementApi
  with StatementRefApi
  with ActorApi
  with ActivityApi
  with Loggable {

  import executionContext.driver.simple._

  /**
   * This method may be called to fetch a multiple Statements.
   * If the statementId or voidedStatementId parameter is specified a single [[Statement]] is returned.
   * @return
   */
  def findStatements (query: StatementQuery): PartialSeq[Statement] =
    db withSession { implicit session =>

      query match {
        case q if q.statementId isDefined => Await.result(
          findStatementById(q.statementId toString), timeout
        ) toPartialSeq

        case q if q.voidedStatementId isDefined => Await.result(
          findVoidedStatement(q.voidedStatementId toString), timeout
        ) toPartialSeq

        case q => Await.result(findStatementsByParams(q), timeout)
      }
    }

  /**
   * Delete the specified profile document in the context of the specified [[Agent]].
   * @param agent The [[Agent]] associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def deleteProfile (agent:     Agent,
                     profileId: String): Unit =
    db withSession { implicit session =>
      actors keyFor agent map { key =>
        deleteDocument     (key, profileId)
        deleteAgentProfile (key, profileId)
      }
    }

  def deleteProfile(agent:        Agent,
                    activityId:   Activity#Id,
                    stateId:      String,
                    registration: Option[UUID]): Unit =
    db withSession { implicit session =>
      actors keyFor agent match {
        case Some(actorKey) =>
          deleteAgentProfile(actorKey, activityId, stateId, registration)

        case None => Unit
      }
    }

  def deleteProfiles(agent: Agent,
                     activityId: String,
                     registration: Option[UUID]): Unit = db.withSession { implicit session =>
    actors keyFor agent match {
      case Some(key) => ???
//        deleteDocument     (key, profileId) //getDocuments(value, activityId, registration, None) map { x => x.key }
//        documents filter { x => x.key inSet docKeys } delete

      case None => Unit
    }
  }


  /**
   * Remove the specified profile document in the context of the specified Activity and remove document
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   */
  def deleteActivityProfile (activityId: String,
                             profileId:  String): Unit =
    db withSession { implicit session =>
      findActivityProfileByActivityIdAndProfileIdQC (activityId, profileId) delete
    }

  /**
   * Loads the complete Activity Object specified.
   * @param activityId The id associated with the Activities to load. (IRI)
   * @return [[Activity]] if found or [[None]]
   */
  def getActivity (activityId: String): Option[Activity] =
    db withSession { implicit session =>
      findActivityById (activityId)
    }

  /**
   * Loads the Activities: id and title.
   * @param activity The filter name.
   * @return List of [[Activity]]
   */
  def getActivities (activity: String): Seq[Activity] =
    db withSession { implicit session =>
      activities filterByName activity
    }

  /**
   * Stores, fetches, or deletes the document specified by the given stateId that exists
   * in the context of the specified Activity, Agent, and registration (if specified).
   * @param agent The Agent associated with this state.
   * @param activityId The Activity id associated with this state.
   * @param stateId The id for this state, within the given context.
   * @param registration The registration id associated with this state.
   * @return State Content
   */
  def getDocument(agent:        Agent,
                  activityId:   String,
                  stateId:      String,
                  registration: Option[UUID]): Option[Document] =
    db withSession { implicit session =>
      actors keyFor agent match {
        case Some(actorKey) => findDocument (actorKey, activityId, stateId, registration)
        case None => None
      }
    }

  /**
   * Get the specified profile document in the context of the specified Activity.
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   * @return [[Document]] or [[None]] if not found
   */
  def getDocument(activityId: Activity#Id,
                  profileId:  ProfileId): Option[Document] =
    db withSession { implicit session =>
      findDocument(activityId, profileId)
    }

  /**
   * Fetches ids of all state data for this context (Activity + Agent [ + registration if specified]).
   * If "since" parameter is specified, this is limited to entries that have been stored or
   * updated since the specified timestamp (exclusive).
   * @param agent The Agent associated with these states.
   * @param activityId The Activity id associated with these states.
   * @param registration The registration id associated with these states.
   * @param since Only ids of states stored since the specified timestamp (exclusive) are returned.
   * @return List of ids
   */
  def getDocuments(agent:        Agent,
                   activityId:   Activity#Id,
                   registration: Option[UUID],
                   since:        Option[DateTime]): Seq[String] =
    db.withSession { implicit session =>
      actors keyFor agent match {
        case Some(value) => findDocumentKeys(value, activityId, registration, since)
        case None => Seq()
      }
    }

  /**
   * Loads ids of all profile entries for an [[Activity]].
   * If "since" parameter is specified, this is limited to entries that have been stored
   * or updated since the specified timestamp (exclusive).
   * @param activityId The [[Activity.id]] associated with these profiles.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return List of profile ids
   */
  def getProfileIds(activityId: Activity#Id,
                    since:      Option[DateTime] = None): Seq[ProfileId] =
    db withSession { implicit session =>

      since map { dt =>
        findActivityProfileByUpdateAndProfileIdQC (dt, activityId) run

      } getOrElse {
        findActivityProfileByProfileIdQC (activityId) run

      }
    }


}