package com.arcusys.valamis.lrs.jdbc.database.api.query

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.StatementObjectRow

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait StatementObjectQueries extends TypeAliases {
  this: LrsDataContext =>

  import driver.simple._

  private type KeyCol = ConstColumn[StatementObjectRow#Type]

  private def getTypeQ (key: KeyCol) = statementObjects filter {
    x => x.key === key
  } map {
    x => x.objectType
  }

  def findStatementObjectTypesQ (keys: Seq[StatementObjectRow#Type]) =
    statementObjects filter {
      x => x.key inSet keys
    }

  val getStatementObjectTypeQC = Compiled((key: KeyCol) => getTypeQ(key))
}
