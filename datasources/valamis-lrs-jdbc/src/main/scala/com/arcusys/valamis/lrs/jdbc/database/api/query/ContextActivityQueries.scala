package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.ContextRow

/**
 * Created by Iliya Tryapitsin on 15.07.15.
 */
trait ContextActivityQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type ContextKeyCol = ConstColumn[ContextRow#Type]

  def findContextActivitiesByContextKeyQ (key: ContextKeyCol) =
    contextActivities filter {
      x => x.contextKey === key
    } join activities on { (x1, x2) => x1.activityKey === x2.key }

  def findContextActivitiesByContextKeysQ (keys: Seq[ContextRow#Type]) =
    contextActivities filter {
      x => x.contextKey inSet keys
    } join activities on {
      (x1, x2) => x1.activityKey === x2.key
    }

  val findContextActivitiesByContextKeyQC = Compiled(
    (key: ContextKeyCol) => findContextActivitiesByContextKeyQ(key)
  )
}
