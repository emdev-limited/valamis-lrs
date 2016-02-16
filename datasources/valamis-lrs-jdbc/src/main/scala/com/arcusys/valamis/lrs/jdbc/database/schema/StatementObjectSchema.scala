package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.StatementObjectRow
import com.arcusys.valamis.lrs.tincan.StatementObjectType.{Type => ObjType}
/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait StatementObjectSchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  class StatementObjectsTable(tag: Tag) extends LongKeyTable[StatementObjectRow](tag, tblName("statementObjects")) {

    def objectType = column[ObjType]("objectType", O.NotNull, O.DBType(varCharMax))

    def * = (key.?, objectType) <> (StatementObjectRow.tupled, StatementObjectRow.unapply)
  }

  lazy val statementObjects = TableQuery[StatementObjectsTable]
}
