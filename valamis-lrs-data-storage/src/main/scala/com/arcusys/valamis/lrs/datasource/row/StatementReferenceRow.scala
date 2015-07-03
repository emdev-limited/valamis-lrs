package com.arcusys.valamis.lrs.datasource.row

import java.util.UUID

import com.arcusys.valamis.lrs.datasource.WithRequireKey
import com.arcusys.valamis.lrs.tincan.StatementReference

/**
 * Created by igorborisov on 12.01.15.
 */

case class StatementReferenceRow(key: StatementObjectRow#Type,
                                 statementId: StatementRow#Type) extends WithRequireKey[StatementObjectRow#Type] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[Type])

  def toModel = StatementReference(UUID.fromString(statementId))
}

