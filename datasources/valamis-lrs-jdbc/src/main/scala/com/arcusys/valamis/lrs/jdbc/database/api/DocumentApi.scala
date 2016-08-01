package com.arcusys.valamis.lrs.jdbc.database.api

import java.util.UUID

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.DocumentQueries
import com.arcusys.valamis.lrs.jdbc.database.row.ActorRow
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait DocumentApi extends DocumentQueries {
  this: LrsDataContext =>

  import driver.simple._

  /**
   * Delete Tincan [[com.arcusys.valamis.lrs.tincan.Document]] from a storage
   * @param actorKey Actor storage key
   * @param profileId Tincan ProfileId
   * @param session
   * @return Result code is a success if it great zero
   */
  def deleteDocument (actorKey: ActorRow#Type, profileId: ProfileId)
                     (implicit session: Session) =
    findDocumentsByActorAndProfileIdQC (actorKey, profileId) delete

  def findDocument (activityId: Activity#Id,
                    profileId:  ProfileId)
                   (implicit session: Session): Option[Document] =
    findDocumentsByActivityIdAndProfileIdQC (activityId, profileId).firstOption map {
      x => x convert
    }

  def findDocumentKeys (actorKey:     ActorRow#Type,
                        activityId:   Activity#Id,
                        registration: Option[UUID],
                        since:        Option[DateTime])
                       (implicit session: Session): Seq[String] = {
    val keys = for {
      reg <- registration
      s   <- since
    } yield findDocumentKeysByActorAndActivityIdAndRegistrationAndSinceQC (
      (actorKey, activityId, reg toString, s)
    ).run

    keys getOrElse {
      findDocumentKeysByActorAndActivityIdQC ((actorKey, activityId)) run
    }
  }

  def findDocument (actorKey:     ActorRow#Type,
                    activityId:   Activity#Id,
                    stateId:      String,
                    registration: Option[UUID])
                   (implicit session: Session): Option[Document] = registration map { reg =>
    findDocumentsByActorAndActivityIdAndStateIdAndRegistrationQC((actorKey, activityId, stateId, reg toString)) firstOption
  } getOrElse {
    findDocumentsByActorAndActivityIdAndStateIdQC (actorKey, activityId, stateId) firstOption
  } map { x => x convert }

  def findDocumentKeys (actorKey: ActorRow#Type,
                        activityId: Activity#Id,
                        stateId: String,
                        registration: Option[UUID])
                       (implicit session: Session) = registration map { reg =>
    findDocumentKeysByActorAndActivityIdAndStateIdAndRegistrationQC((actorKey, activityId, stateId, reg toString)) run
  } getOrElse {
    findDocumentKeysByActorAndActivityIdAndStateIdQC (actorKey, activityId, stateId) run
  }
}
