package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.{StatementRow, StatementReferenceRow}

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait StatementReferenceSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class StatementReferenceTable(tag: Tag) extends LongKeyTable[StatementReferenceRow](tag, tblName("stmntRefs"), false) {
    override def * = (key, statementId) <>(StatementReferenceRow.tupled, StatementReferenceRow.unapply)

    def statementId = column[StatementRow#Type]("statementId", O.DBType(uuidKeyLength))

    def statementObject = foreignKey(fkName("stmntRef2stmntObj"), key, TableQuery[StatementObjectsTable])(statement => statement.key)

    def indx = index(idxName("stmntRef"), (key, statementId))
  }

  lazy val statementReferences = TableQuery[StatementReferenceTable]
}
