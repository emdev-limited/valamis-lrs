package com.arcusys.valamis.lrs.jdbc.database.api

import java.net.URI

import com.arcusys.valamis.lrs.{ApplicationIdThreadLocal, _}
import com.arcusys.valamis.lrs.jdbc.JdbcSecurityManager
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query._
import com.arcusys.valamis.lrs.jdbc.database.row.ActivityRow
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.ApplicationIdThreadLocal

/**
 * LRS component for a Tincan [[Activity]]
 * @author Created by Iliya Tryapitsin on 08.07.15.
 */
trait ActivityApi extends ActivityQueries with TypeAliases{
  this: LrsDataContext
    with StatementObjectApi
    with ActorApi =>

  import driver.simple._

  val securityManager =  new JdbcSecurityManager(driver, db)
  /**
   * Load Tincan [[Activity]] by [[Activity.id]]
   * @param activityId [[Activity.id]]
   * @param session
   * @return Tincan [[Activity]] instance
   */
  def findActivityById (activityId: String)
                       (implicit session: Session): Option[Activity] =
    findActivityByIdQC (activityId).firstOption map { x => x.convert }

  /**
   * Load Tincan [[Activity]] by storage key
   * @param key Storage key
   * @param session
   * @return Tincan [[Activity]] instance
   */
  def findActivityByKey (key: ActivityRow#Type)
                        (implicit session: Session): Activity =
    findActivityByKeyQC (key).first convert

  /**
   * Load Tincan [[Activity]] list by keys list
   * @param keys [[Activity]] keys
   * @param session
   * @return [[Activity]] with storage key list
   */
  def findActivitiesByKeys (keys: Seq[ActivityRow#Type])
                           (implicit session: Session): Map[ActivityRow#Type, Activity] =
    if (keys isEmpty) Map()
    else findActivitiesByKeysQ (keys).run map { x => (x.key, x convert) } toMap


  implicit class ActivityInsertQueries (q: ActivityQ) {


    def filterByName (name: String)
                     (implicit session: Session): Seq[Activity]= {
      val query = if (name.isEmpty) activities filter { x => x.name notEmpty } take TakeCount
      else activities filter { x => x.name like name } take TakeCount

      query.run map { x => x.convert }
    }

    def keyFor (activity: Activity)
               (implicit session: Session): Option[ActivityRow#Type] =
      q keyFor activity.id

    def keyFor (uri: String)
               (implicit session: Session): Option[ActivityRow#Type] =
      findActivityKeyByIdQC (uri) firstOption

    def keyFor (uri: Option[URI])
               (implicit session: Session): Option[ActivityRow#Type] =
      uri match {
        case Some(u) => q keyFor u.toString
        case None    => None
      }

    def keysFor (ids: Seq[String])
                (implicit session: Session): Seq[(String, Option[ActivityRow#Type])] = {
      val queryResult = q filter { x =>
        x.id inSet ids
      } map {
        x => (x.id, x.key)
      } run

      val savedIds = queryResult map { x => x._1 }

      val notSaved = ids filterNot { x =>
        savedIds contains x

      } map { x =>
        (x, None)
      }

      val saved = queryResult map { x =>
        (x._1, x._2 ?)
      }

      saved ++ notSaved
    }

    def findNotSaved (ids: Seq[String])
                     (implicit session: Session): Seq[String] = {
      val saved = q filter { x =>
        x.id inSet ids
      } map {
        x => x.id
      } list

      ids filterNot { id => saved contains id }
    }

    /**
     * Insert Tincan Activity to the storage
     * @param activityId Tincan Activity identity
     * @param session
     * @return Identity key in the storage
     */
    def add (activityId: String)
            (implicit session: Session): ActivityRow#Type =
      q add Activity(activityId)

    /**
     * Insert Tincan Activity to the storage
     * @param activity Activity instance
     * @param session
     * @return Identity key in the storage
     */
    def add (activity: Activity)
            (implicit session: Session): ActivityRow#Type =
      activity.convert withKey {
        statementObjects add activity

      } build { r =>
        q += r
      }

    /**
     * Bulk insert Activity set to the storage
     * @param activities Activity set
     * @param session
     * @return List of pairs identity key and Activity id
     */
    def addSeq (activities: Seq[Activity])
               (implicit session: Session): Seq[(String, ActivityRow#Type)] = {
      val keys = statementObjects addSeq activities
      keys zip activities map { x =>
        x._2.convert withKey x._1 build

      } afterThat { x =>
        q ++= x
        x
      } map { x =>
        (x.id, x.key)
      }
    }

    /**
     * Insert unique Tincan Activity to the storage.
     * @param activity Activity instance
     * @param session
     * @return Identity key in the storage
     */
    def addOrUpdate(activity: Activity)
                 (implicit session: Session): ActivityRow#Type = {
      val key = q keyFor activity

      key match {
        case Some(k) => {

          lazy val application = securityManager.getApplication(ApplicationIdThreadLocal.getApplicationId)

          if (application.isDefined && application.get.scope.contains(AuthorizationScope.Define)) {
            val newActivity = new ActivityRow(
              key = k,
              id = activity.id,
              name = activity.name,
              description = activity.description,
              theType = activity.theType,
              moreInfo = activity.moreInfo,
              interactionType = activity.interactionType,
              correctResponsesPattern = activity.correctResponsesPattern,
              choices = activity.choices,
              source = activity.source,
              target = activity.target,
              steps = activity.steps,
              extensions = activity.extensions)

            q.filter(x => x.key === k).update(newActivity)
          }
          k
        }
        case _ => q add activity
      }
    }
  }
}
