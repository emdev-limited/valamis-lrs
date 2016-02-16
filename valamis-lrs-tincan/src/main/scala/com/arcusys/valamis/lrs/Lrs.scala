package com.arcusys.valamis.lrs

import java.util.UUID

import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 20.07.15.
 */
trait Lrs {

  /**
   * This method may be called to fetch a multiple Statements.
   * If the statementId or voidedStatementId parameter is specified a single [[Statement]] is returned.
   * @param query
   * @return
   */
  def findStatements (query: StatementQuery): PartialSeq[Statement]

  /**
   * Delete the specified profile document in the context of the specified [[Agent]].
   * @param agent The [[Agent]] associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def deleteProfile (agent:     Agent,
                     profileId: ProfileId): Unit

  def deleteProfile(agent:        Agent,
                    activityId:   Activity#Id,
                    stateId:      String,
                    registration: Option[UUID]): Unit

  /**
   * Remove the specified profile document in the context of the specified Activity and remove document
   * @param activityId The activity id associated with these profiles.
   * @param profileId  The profile id associated with this profile.
   */
  def deleteActivityProfile (activityId: Activity#Id,
                             profileId:  ProfileId): Unit

  /**
   * Loads the complete Activity Object specified.
   * @param activityId The id associated with the Activities to load. (IRI)
   * @return [[Activity]] if found or [[None]]
   */
  def getActivity (activityId: Activity#Id): Option[Activity]

  /**
   * Loads the Activities: id and title.
   * @param activity The filter name.
   * @return List of [[Activity]]
   */
  def getActivities (activity: String): Seq[Activity]

  /**
   * Loads ids of all profile entries for an [[Activity]].
   * If "since" parameter is specified, this is limited to entries that have been stored
   * or updated since the specified timestamp (exclusive).
   * @param activityId The [[Activity.id]] associated with these profiles.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return List of profile ids
   */
  def getProfileIds(activityId: Activity#Id,
                    since:      Option[DateTime] = None): Seq[String]

  /**
   * Get the specified profile document in the context of the specified Activity.
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   * @return [[Document]] or [[None]] if not found
   */
  def getDocument(activityId: Activity#Id,
                  profileId:  ProfileId): Option[Document]

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
                  registration: Option[UUID]): Option[Document]

  /**
   * Add or Update if exist the specified profile document in the context of the specified Activity.
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   * @param doc The new document version
   */
  def addOrUpdateDocument(activityId: Activity#Id,
                          profileId:  ProfileId,
                          doc:        Document): Unit

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
  def getPerson(agent: Agent): Person

  /**
   * Loads ids of all profile entries for an Agent. If "since" parameter is specified,
   * this is limited to entries that have been stored or updated since the specified timestamp (exclusive).
   * @param agent The Agent associated with this profile.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return Lis of ids
   */
  def getProfiles(agent: Agent,
                  since: Option[DateTime] = None): Seq[ProfileId]

  /**
   * Get the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   */
  def getProfileContent(agent:     Agent,
                        profileId: ProfileId): Option[Document]

  /**
   * Store the specified profile document in the context of the specified Agent.
   * @param agent The Agent associated with this profile.
   * @param profileId The profile id associated with this profile.
   * @param doc The document of profile
   */
  def addOrUpdateDocument(agent:     Agent,
                          profileId: ProfileId,
                          doc:       Document): Unit

  /**
   * Add or Update if exist the specified profile document in the context of the specified Activity.
   * @param agent The agent associated with these profiles.
   * @param activityId The activity id associated with these profiles.
   * @param stateId The state id associated with this profile.
   * @param registration The registration UUID associated with this profile.
   * @param doc The new document version
   */
  def addOrUpdateDocument(agent:        Agent,
                          activityId:   Activity#Id,
                          stateId:      String,
                          registration: Option[UUID] = None,
                          doc:          Document): Unit
}
