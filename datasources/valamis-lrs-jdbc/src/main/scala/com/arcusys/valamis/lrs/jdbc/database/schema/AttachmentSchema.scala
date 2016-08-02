package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.{StatementRow, AttachmentRow}
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait AttachmentSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  lazy val attachments = TableQuery[AttachmentsTable]

  class AttachmentsTable(tag: Tag) extends LongKeyTable[AttachmentRow](tag: Tag, tblName("attachments")) {

    override def * = (key.?, statementKey, usageType, display, description.?, content, length, sha2, fileUrl.?) <>(AttachmentRow.tupled, AttachmentRow.unapply)

    def statementKey = column[StatementRow#Type]("statementId", O.NotNull, O.DBType(uuidKeyLength))
    def usageType    = column[String]("usageType", O.DBType(varCharMax))
    def display      = column[LanguageMap]("display", O.DBType(varCharMax))
    def description  = column[LanguageMap]("description", O.DBType(varCharMax), O.Nullable)
    def content      = column[String]("content", O.DBType(varCharMax))
    def length       = column[Int]("length")
    def sha2         = column[String]("sha2", O.DBType(varCharMax))
    def fileUrl      = column[String]("fileUrl", O.Nullable, O.DBType(varCharMax))

    def statement = foreignKey(fkName("atchmnt2stmnt"), statementKey, TableQuery[StatementsTable])(statement => statement.key)
  }
}
