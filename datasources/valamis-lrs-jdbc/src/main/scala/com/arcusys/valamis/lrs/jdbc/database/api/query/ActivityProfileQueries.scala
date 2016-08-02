package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 13.07.15.
 */
trait ActivityProfileQueries extends TypeAliases {
  this: LrsDataContext =>

  import driver.simple._
  import jodaSupport._

  private type UpdateCol = ConstColumn[DateTime]
  private type IdCol     = ConstColumn[String]

  def findActivityProfileByUpdateAndIdQ (since: UpdateCol,
                                         id:    IdCol) =
    activityProfiles join activities on {
      (x1, x2) => x1.activityKey   === x2.key

    } join documents on {
      case ((x1, x2),  x3) => x1.documentKey === x3.key

    } filter {
      case ((x1, x2), x3) => x3.updated >= since && x2.id === id

    } groupBy {
      case ((x1, x2), x3) => x1.profileId

    } map { case (k, v) => k }

  def findActivityProfileByProfileIdQ (id: IdCol) =
    activityProfiles join activities on {
      (x1, x2) => x1.activityKey   === x2.key

    } filter {
      case (x1, x2) => x2.id === id

    } groupBy {
      case (x1, x2) => x1.profileId

    } map { case (k, v) => k }

  def findActivityProfileQ (activityId: IdCol,
                            profileId:  IdCol) =
    activityProfiles filter { x =>
      x.profileId === profileId

    } filter { x =>
      x.activity filter { a => a.id === activityId } exists

    } join documents on { (x1, x2) => x1.documentKey === x2.key }


  val findActivityProfileByActivityIdAndProfileIdQC = Compiled (findActivityProfileQ _)

  val findActivityProfileByUpdateAndProfileIdQC = Compiled(findActivityProfileByUpdateAndIdQ _)

  val findActivityProfileByProfileIdQC = Compiled(findActivityProfileByProfileIdQ _)
}
