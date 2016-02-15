package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan.{Constants => C}

import scala.slick.lifted.ProvenShape

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait ActorsSchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  class ActorsTable(tag: Tag) extends LongKeyTable[ActorRow](tag: Tag, tblName("actors"), false) {

    override def * = ProvenShape.proveShapeOf(
      (key, name.?, descriptor, mBox.?, mBoxSha1Sum.?, openId.?, accountKey.?, groupKey.?)
        .<>[ActorRow, (ActorRow#Type, Option[String], String, Option[String], Option[String], Option[String], AccountRow#KeyType, Option[GroupRow#Type])](
      {
        case (key, name, actor, mBox, mBoxSha1Sum, openId, accountKey, groupKey) => actor match {
          case C.Tincan.Group => GroupRow(key, name, mBox, mBoxSha1Sum, openId, accountKey).asInstanceOf[ActorRow]
          case _              => AgentRow(key, name, mBox, mBoxSha1Sum, openId, accountKey, groupKey).asInstanceOf[ActorRow]
        }
      }, {
        case AgentRow(key, name, mBox, mBoxSha1Sum, openId, accountKey, groupKey) =>
          Option((key, name, C.Tincan.Agent, mBox, mBoxSha1Sum, openId, accountKey, groupKey))

        case GroupRow(key, name, mBox, mBoxSha1Sum, openId, accountKey) =>
          Option((key, name, C.Tincan.Group, mBox, mBoxSha1Sum, openId, accountKey, None))
      }))

    def account         = foreignKey(fkName("actor2account"),  accountKey, TableQuery[AccountsTable])        (_.key)
    def group           = foreignKey(fkName("actor2group"),    groupKey,   TableQuery[ActorsTable])          (_.key)
    def statementObject = foreignKey(fkName("actor2stmntObj"), key,        TableQuery[StatementObjectsTable])(_.key)

    def accountKey  = column[AccountRow#Type]("accountKey", O.Nullable)
    def groupKey    = column[GroupRow#Type]  ("groupKey",   O.Nullable)
    def name        = column[String](C.Tincan.Field.name,   O.Nullable, O.DBType(varCharMax))
    def descriptor  = column[String](C.Tincan.Field.descriptor, O.NotNull, O.DBType(varCharMax))
    def mBox        = column[String]("mBox", O.Nullable, O.DBType(varCharMax))
    def mBoxSha1Sum = column[String]("mBoxSha1Sum", O.Nullable, O.DBType(varCharMax))
    def openId      = column[String]("openId", O.Nullable, O.DBType(varCharMax))
  }

  lazy val actors = TableQuery[ActorsTable]
}
