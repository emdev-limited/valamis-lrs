package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{AgentRow, GroupRow, AccountRow, StatementObjectRow}
import com.arcusys.valamis.lrs.tincan.Agent

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object AgentConverter {
  implicit def asRow(value: Agent,
                     statementObjectKey: StatementObjectRow#Type,
                     account: Option[AccountRow#Type],
                     groupKey: Option[GroupRow#Type]): AgentRow = AgentRow(
    statementObjectKey,
    value.name,
    value.mBox,
    value.mBoxSha1Sum,
    value.openId,
    account, 
    groupKey)
}
