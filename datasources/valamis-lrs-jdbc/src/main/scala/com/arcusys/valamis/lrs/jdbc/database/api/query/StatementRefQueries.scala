package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.StatementReferenceRow

/**
 * Created by Iliya Tryapitsin on 09.07.15.
 */
trait StatementRefQueries extends TypeAliases {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  private type KeyCol = ConstColumn[StatementReferenceRow#Type]

  /**
   * Find Statement Reference by storage key
   * @param key Storage key
   * @return Statement reference storage record
   */
  def findStatementRefByKeyQ (key: KeyCol) =
    statementReferences filter {
      x => x.key === key
    }

  def findStatementRefsByKeysQ (keys: Seq[StatementReferenceRow#Type]) =
    statementReferences filter {
      x => x.key inSet keys
    }

  /**
   * Compiled query for a find Statement Reference by storage key
   */
  val findStatementRefByKeyQC = Compiled ((key: KeyCol) => findStatementRefByKeyQ(key))
}
