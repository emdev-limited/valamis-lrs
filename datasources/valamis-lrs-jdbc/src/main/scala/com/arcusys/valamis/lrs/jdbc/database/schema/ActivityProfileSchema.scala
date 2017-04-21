package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait ActivityProfileSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._
  import ForeignKeyAction._

  class ActivityProfilesTable(tag: Tag) extends Table[ActivityProfileRow](tag, tblName("activityProfiles")) {
    override def *  = (
      activityKey,
      profileId  ,
      documentKey
    ) <> (ActivityProfileRow.tupled, ActivityProfileRow.unapply)

    def activityKey = column [ActivityRow#Type] ("activityKey")
    def profileId   = column [String]           ("profileId"  , O.DBType(varCharPk))
    def documentKey = column [DocumentRow#Type] ("documentKey", O.DBType(uuidKeyLength))

    def activity = foreignKey(fkName("activityProfile2activity" ), activityKey, TQ[ActivitiesTable]) (x => x.key, onUpdate = Restrict, onDelete = Cascade)
    def document = foreignKey(fkName("activityProfiles2document"), documentKey, TQ[DocumentsTable ]) (x => x.key, onUpdate = Restrict, onDelete = Cascade)

    def indx = index(idxName("activityProfile"), (activityKey, profileId), unique = true)
  }

  lazy val activityProfiles = TableQuery[ActivityProfilesTable]
}
