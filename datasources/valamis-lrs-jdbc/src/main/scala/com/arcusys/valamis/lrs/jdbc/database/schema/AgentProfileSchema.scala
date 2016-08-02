package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait AgentProfileSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._
  import ForeignKeyAction._

  class AgentProfilesTable(tag: Tag) extends Table[AgentProfileRow](tag, tblName("agentProfiles")) {

    def * = (profileId, agentKey, documentKey) <>(AgentProfileRow.tupled, AgentProfileRow.unapply)

    def profileId   = column[String]("profileId", O.NotNull, O.DBType(varCharPk))
    def agentKey    = column[AgentRow#Type]("agentKey")
    def documentKey = column[DocumentRow#Type]("documentKey", O.DBType(uuidKeyLength))

    def document = foreignKey(fkName("agentProfile2document"), documentKey, TQ[DocumentsTable])(_.key, onUpdate = Restrict, onDelete = Cascade)
    def agent    = foreignKey(fkName("agentProfile2agent"),    agentKey,    TQ[ActorsTable])   (_.key, onUpdate = Restrict, onDelete = Cascade)

    def indx = index(idxName("agentProfile"), (profileId, agentKey, documentKey), unique = true)
  }

  lazy val agentProfiles = TQ[AgentProfilesTable]

}
