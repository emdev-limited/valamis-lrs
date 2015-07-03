package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{StatementObjectRow, AccountRow, GroupRow}
import com.arcusys.valamis.lrs.tincan.Group

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object GroupConverter {
  implicit def asRow(value: Group,
            statementObjectKey: StatementObjectRow#Type,
            account: AccountRow#KeyType): GroupRow = GroupRow(
    statementObjectKey, 
    value.name, 
    value.mBox,
    value.mBoxSha1Sum,
    value.openId,
    account)
}
