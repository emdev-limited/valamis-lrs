package com.arcusys.valamis.lrs.services

import com.arcusys.valamis.lrs.converter.ActivityConverter._
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.lrs.datasource.row.{ActivityProfileRow, DocumentRow}
import com.arcusys.valamis.lrs.tincan.{Activity, ContentType, Document}
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 02/02/15.
 */
private[lrs] trait ActivityProfileComponent {
  this: DataContext with StatementSaverComponent =>

  import driver.simple._
  import jodaSupport._

  /**
   * Loads the complete Activity Object specified.
   * @param activityId The id associated with the Activities to load. (IRI)
   * @return [[Activity]] if found or [[None]]
   */
  def getActivity(activityId: String): Option[Activity] = db.withSession(implicit session =>
      activities
      .filter(x => x.id === activityId)
      .firstOption
      .map(x => asModel(x)))

  /**
   * Loads the Activities: id and title.
   * @param activity The filter name.
   * @return List of [[Activity]]
   */
  def getActivities(activity: String): Seq[Activity] = db.withSession(implicit session => {
      activities
      .list
      .map(x => asModel(x))
      .filter(act => act.name.isDefined && activity != null && act.name.get.exists(_._2.toLowerCase.startsWith(activity.toLowerCase)))
    }
  )

  /**
   * Loads ids of all profile entries for an Activity.
   * If "since" parameter is specified, this is limited to entries that have been stored
   * or updated since the specified timestamp (exclusive).
   * @param activityId The activity id associated with these profiles.
   * @param since Only ids of profiles stored since the specified timestamp (exclusive) are returned.
   * @return List of profile ids
   */
  def getProfileIds(activityId: String,
                    since: Option[DateTime] = None): Seq[String] = db.withSession(implicit session => {

    val queryWithFilter = since match {
      case Some(v) => activityProfileQuery.filter(x => x._2.updated >= v)
      case None => activityProfileQuery
    }

    queryWithFilter
      .filter(x => x._1._2.id === activityId)
      .map(x => x._1._1.profileId).list.distinct
  })

  /**
   * Get the specified profile document in the context of the specified Activity.
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   * @return [[Document]] or [[None]] if not found
   */
  def getDocument(activityId: String,
                  profileId: String): Option[Document] = db
    .withSession(implicit session => getDocumentRow(activityId, profileId).map(x => x.toModel))

  /**
   * Add or Update if exist the specified profile document in the context of the specified Activity.
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   * @param doc The new document version
   */
  def addOrUpdateDocument(activityId: String,
                          profileId: String,
                          doc: Document): Unit = db.withSession(implicit session => {

    getDocumentRow(activityId, profileId) match {
      case None => {
        val activityKey = getStatementObjectKey(Activity(id = activityId))

        documents.insert(DocumentRow(doc.id.toString, contents = doc.contents, cType = doc.cType))
        activityProfiles.insert(ActivityProfileRow(activityKey, profileId, doc.id.toString))
      }
      case Some(document) => {
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
    }
  })

  /**
   * Remove the specified profile document in the context of the specified Activity and remove document
   * @param activityId The activity id associated with these profiles.
   * @param profileId The profile id associated with this profile.
   */
  def deleteProfileActivityWithDocument(activityId: String,
                                        profileId: String) = db.withSession(implicit session => {
    getDocumentRow(activityId, profileId) match {
      case None    => Unit
      case Some(d) => documents filter { x => x.key === d.key } delete
    }
  })

  private def activityProfileQuery(implicit session: Session) =
    activityProfiles
    .join(activities).on((ap, a) => ap.activityKey   === a.key)
    .join(documents ).on((a,  d) => a._1.documentKey === d.key)

  private def filterActivityProfileQuery(activityId: String,
                                         profileId: String)
                                        (implicit session: Session) =
    activityProfileQuery
      .filter(j => j._1._2.id === activityId && j._1._1.profileId === profileId)

  private def getDocumentQuery(activityId: String,
                               profileId: String)
                              (implicit session: Session) =
    filterActivityProfileQuery(activityId, profileId).map(x => x._2)

  private def getDocumentRow(activityId: String,
                             profileId: String)
                            (implicit session: Session) =
    getDocumentQuery(activityId, profileId).firstOption

}
