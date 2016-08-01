package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.{ScoreRow, ResultRow}

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait ResultSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class ResultsTable(tag: Tag) extends LongKeyTable[ResultRow](tag, tblName("results")) {
    def * = (key.?, scoreKey, success.?, completion.?, response.?, duration.?, extensions.?) <>(ResultRow.tupled, ResultRow.unapply)

    def scoreKey   = column[ScoreRow#KeyType]("scoreKey", O.Nullable)
    def success    = column[Boolean]("success", O.Nullable)
    def completion = column[Boolean]("completion", O.Nullable)
    def response   = column[String]("response", O.Nullable, O.DBType(varCharMax))
    def duration   = column[String]("duration", O.Nullable, O.DBType(varCharMax))
    def extensions = column[Map[String, String]]("extensions", O.Nullable, O.DBType(varCharMax))

    def score = foreignKey(fkName("result2score"), scoreKey, TableQuery[ScoresTable])(score => score.key)
  }

  lazy val results = TableQuery[ResultsTable]
}
