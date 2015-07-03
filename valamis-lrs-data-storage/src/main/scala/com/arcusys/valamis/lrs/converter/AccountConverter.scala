package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.AccountRow
import com.arcusys.valamis.lrs.tincan.Account

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object AccountConverter {
  implicit def asRow(value: Account): AccountRow = AccountRow(
    homepage = value.homePage, 
    name = value.name)

  implicit def asModel(value: AccountRow): Account = Account(
    homePage = value.homepage,
    name = value.name)
}
