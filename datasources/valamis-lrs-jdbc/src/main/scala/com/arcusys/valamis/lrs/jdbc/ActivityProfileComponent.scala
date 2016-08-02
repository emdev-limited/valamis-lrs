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

  import driver.simple._
//  import jodaSupport._



  def activityProfileQuery(implicit session: Session) =
    activityProfiles
    .join(activities).on((ap, a) => ap.activityKey   === a.key)
    .join(documents ).on((a,  d) => a._1.documentKey === d.key)

  def filterActivityProfileQuery(activityId: String,
                                         profileId: String)
                                        (implicit session: Session) =
    activityProfileQuery
      .filter(j => j._1._2.id === activityId && j._1._1.profileId === profileId)

  def getDocumentQuery(activityId: String,
                               profileId: String)
                              (implicit session: Session) =
    filterActivityProfileQuery(activityId, profileId).map(x => x._2)

  def getDocumentRow(activityId: String,
                             profileId: String)
                            (implicit session: Session) =
    getDocumentQuery(activityId, profileId).firstOption

}
