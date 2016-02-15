package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait SubStatementSchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  class SubStatementsTable(tag: Tag) extends LongKeyTable[SubStatementRow](tag, tblName("subStatements"), false) {
    override def * = (key, statementObjectKey, actorKey, verbId, verbDisplay) <>(SubStatementRow.tupled, SubStatementRow.unapply)

    def statementObjectKey = column[StatementObjectRow#Type]("statementObject")
    def actorKey = column[ActorRow#Type]("actorId", O.NotNull)
    def verbId = column[String]("verbId", O.NotNull, O.DBType(varCharMax))
    def verbDisplay = column[LanguageMap]("verbDisplay", O.NotNull, O.DBType(varCharMax))

    def actor = foreignKey(fkName("subStmnt2actor"), actorKey, TableQuery[ActorsTable])(_.key)
    def statementObject = foreignKey(fkName("subSstmnt2stmntObj"), statementObjectKey, TableQuery[StatementObjectsTable])(_.key)
  }

  lazy val subStatements = TableQuery[SubStatementsTable]

}
