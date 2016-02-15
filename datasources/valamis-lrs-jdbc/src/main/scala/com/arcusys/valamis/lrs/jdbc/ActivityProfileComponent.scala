package com.arcusys.valamis.lrs.jdbc

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api._
import com.arcusys.valamis.lrs.jdbc.database.row.{DocumentRow, ActivityProfileRow}
import com.arcusys.valamis.lrs.tincan.{Activity, ContentType, Document}
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 02/02/15.
 */
@deprecated
private[lrs] trait ActivityProfileComponent
  extends StatementObjectApi
  with StatementApi
  with ResultApi
  with ScoreApi
  with AttachmentApi
  with AccountApi
  with ContextApi
  with SubStatementApi
  with StatementRefApi
  with ActorApi
  with ActivityApi
  with Loggable {
  this: LrsDataContext =>

  import executionContext.driver.simple._
//  import jodaSupport._



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
        val activityKey = statementObjects addExt Activity(id = activityId)

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


  def activityProfileQuery(implicit session: Session) =
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
