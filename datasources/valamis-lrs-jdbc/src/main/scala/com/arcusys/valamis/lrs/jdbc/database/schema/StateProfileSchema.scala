package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait StateProfileSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class StateProfilesTable(tag: Tag) extends Table[StateProfileRow](tag, tblName("stateProfiles")) {
    override def * = (stateId, agentKey, activityKey, registration, documentKey) <> (StateProfileRow.tupled, StateProfileRow.unapply)

    def agentKey = column[AgentRow#Type]("agentKey")
    def activityKey = column[ActivityRow#Type]("activityKey")
    def stateId = column[String]("stateId", O.DBType(varCharPk))
    def registration = column[Option[String]]("registration", O.DBType(varCharPk))
    def documentKey = column[DocumentRow#Type]("documentKey" , O.DBType(uuidKeyLength))

    def activity = foreignKey(fkName("stateProfile2activity"), activityKey, TableQuery[ActivitiesTable])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def document = foreignKey(fkName("stateProfiles2document"), documentKey, TableQuery[DocumentsTable])(x => x.key, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
    def agent = foreignKey(fkName("stateProfile2agent"), agentKey, TableQuery[ActorsTable])(_.key)

    def indx = index(idxName("stateProfile"), (agentKey, activityKey, stateId, registration), unique = true)
  }

  lazy val stateProfiles = TableQuery[StateProfilesTable]

}
