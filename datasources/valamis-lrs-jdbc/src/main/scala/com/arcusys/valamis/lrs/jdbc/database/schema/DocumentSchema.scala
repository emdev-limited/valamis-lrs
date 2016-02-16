package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.DocumentRow
import com.arcusys.valamis.lrs.tincan.ContentType._
import org.joda.time.DateTime
import com.arcusys.valamis.lrs.tincan.ContentType.{Type => CntType}

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait DocumentSchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._
  import jodaSupport._

  class DocumentsTable(tag: Tag) extends UUIDKeyTable[DocumentRow](tag, tblName("documents")) {

    def * = (key, updated, contents, cType) <>(DocumentRow.tupled, DocumentRow.unapply)

    def updated  = column[DateTime]("updated")
    def contents = column[String]("contents", O.DBType(varCharMax))
    def cType    = column[CntType]("cType", O.DBType(varCharMax))
  }

  lazy val documents = TableQuery[DocumentsTable]
}
