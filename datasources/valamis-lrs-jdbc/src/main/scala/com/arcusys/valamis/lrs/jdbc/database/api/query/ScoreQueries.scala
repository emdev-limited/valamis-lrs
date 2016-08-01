package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ScoreRow

/**
 * Created by Iliya Tryapitsin on 10.07.15.
 */
trait ScoreQueries  extends TypeAliases {
  this: LrsDataContext =>

  import driver.simple._

  private type KeyCol = ConstColumn[ScoreRow#Type]


  def findScoreByKeyQ (key: KeyCol) = scores filter {
    x => x.key === key
  }

  def findScoresByKeysQ (keys: Seq[ScoreRow#Type]) = scores filter {
    x => x.key inSet keys
  }

  val findScoreByKeyQC = Compiled ((key: KeyCol) => findScoreByKeyQ(key))

}
