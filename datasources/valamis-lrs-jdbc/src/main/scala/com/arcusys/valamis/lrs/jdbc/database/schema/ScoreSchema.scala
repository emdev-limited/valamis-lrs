package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row.ScoreRow

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait ScoreSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class ScoresTable(tag: Tag) extends LongKeyTable[ScoreRow](tag, tblName("scores")) {

    def * = (key.?, scaled, raw, min, max) <>(ScoreRow.tupled, ScoreRow.unapply)

    def scaled = column[Option[Float]]("scaled")
    def raw    = column[Option[Float]]("raw"   )
    def min    = column[Option[Float]]("_min"  )
    def max    = column[Option[Float]]("_max"  )
  }

  lazy val scores = TableQuery[ScoresTable]

}
