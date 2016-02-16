package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ResultRow

/**
 * Created by Iliya Tryapitsin on 10.07.15.
 */
trait ResultQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type KeyCol = ConstColumn[ResultRow#Type]


  def findScoreByResultKeyQ (key: KeyCol) = results filter {
    x => x.key === key
  } flatMap {
    x => x.score
  }

  val findScoreByResultKeyQC = Compiled ((key: KeyCol) => findScoreByResultKeyQ(key))
}
