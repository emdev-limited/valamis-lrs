package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ActorRow
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait DocumentQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._
  import jodaSupport._

  private type ActorKeyCol = ConstColumn[ActorRow#Type]
  private type IdCol       = ConstColumn[String]
  private type DateTimeCol = ConstColumn[DateTime]

  private def findByActorAndProfileIdQ (actorKey:  ActorKeyCol,
                                        profileId: IdCol) =
    agentProfiles filter { x =>
      x.profileId  === profileId &&
      x.agentKey   === actorKey

    } flatMap { x => x.document }

  private def findByActorAndActivityIdQ (actorKey:   ActorKeyCol,
                                         activityId: IdCol) = {
    val activityKeyQuery = activities filter {
      y => y.id === activityId
    } map { y => y.key }

    activityProfiles filter {
      x => x.activityKey in activityKeyQuery

    } flatMap { x => x.document }
  }

  def findByActivityIdAndProfileIdQ (activityId: IdCol,
                                     profileId:  IdCol) =
    activityProfiles filter {
      x => x.profileId === profileId

    } filter {
      x => x.activity filter { a => a.id === activityId } exists

    } flatMap { x => x.document }

  def findByActorAndActivityIdAndStateIdAndSinceQ (actorKey:     ActorKeyCol,
                                                   activityId:   IdCol,
                                                   stateId:      IdCol,
                                                   registration: IdCol) = {
    val activityKeyQuery = activities filter {
      y => y.id === activityId
    } map { y => y.key }

    val stateProfilesQuery = stateProfiles filter {
      x => x.activityKey in activityKeyQuery

    } filter { x =>
      x.stateId      === stateId   &&
      x.agentKey     === actorKey  &&
      x.registration === registration

    } map { x => x.documentKey }

    documents filter { x => x.key in stateProfilesQuery }
  }

  def findByActorAndActivityIdAndRegistrationAndSinceQ (actorKey:     ActorKeyCol,
                                                        activityId:   IdCol,
                                                        registration: IdCol,
                                                        since:        DateTimeCol) = {
    val activityKeyQuery = activities filter {
      y => y.id === activityId
    } map { y => y.key }

    val stateProfilesQuery = stateProfiles filter {
      x => x.activityKey in activityKeyQuery

    } filter {
      x => x.registration === registration

    } map { x => x.documentKey }

    documents filter { x => x.key in stateProfilesQuery } filter { x =>
      x.updated >= since
    }
  }


  def findKeysByQ (actorKey:     ActorKeyCol,
                   activityId:   IdCol,
                   stateId:      IdCol) = {
    val activityKeyQuery = activities filter {
      y => y.id === activityId
    } map { y => y.key }

    val stateProfilesQuery = stateProfiles filter {
      x => x.activityKey in activityKeyQuery

    } filter {
      x => x.stateId === stateId && x.agentKey === actorKey

    } map { x => x.documentKey }

    documents filter { x => x.key in stateProfilesQuery }
  }

  val findDocumentsByActorAndProfileIdQC = Compiled(findByActorAndProfileIdQ _)

  val findDocumentsByActivityIdAndProfileIdQC = Compiled(findByActivityIdAndProfileIdQ _)

  val findDocumentKeysByActorAndActivityIdAndStateIdQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol,
     stateId:      IdCol) => findKeysByQ(actorKey, activityId, stateId)  map { x => x.key })


  val findDocumentKeysByActorAndActivityIdAndStateIdAndRegistrationQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol,
     stateId:      IdCol,
     registration: IdCol) =>
      findByActorAndActivityIdAndStateIdAndSinceQ(
        actorKey, activityId, stateId, registration
      ) map { x => x.key })

  val findDocumentKeysByActorAndActivityIdAndRegistrationAndSinceQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol,
     registration: IdCol,
     since:        DateTimeCol) =>
      findByActorAndActivityIdAndRegistrationAndSinceQ(
        actorKey, activityId, registration, since
      ) map { x => x.key })

  val findDocumentKeysByActorAndActivityIdQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol) => findByActorAndActivityIdQ(actorKey, activityId) map { x => x.key })

  val findDocumentsByActorAndActivityIdAndStateIdQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol,
     stateId:      IdCol) => findKeysByQ(actorKey, activityId, stateId))

  val findDocumentsByActorAndActivityIdAndStateIdAndRegistrationQC = Compiled(
    (actorKey:     ActorKeyCol,
     activityId:   IdCol,
     stateId:      IdCol,
     registration: IdCol) =>
      findByActorAndActivityIdAndStateIdAndSinceQ(
        actorKey, activityId, stateId, registration)
      )
}
