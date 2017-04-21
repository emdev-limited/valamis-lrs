package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Account
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.utils.RunningMode
import org.apache.commons.validator.routines.UrlValidator
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object AccountValidator {

  def checkNotNull(jValue: JValue) = {
    jValue \ name     notNull

    jValue \ homePage notNull
  }

  def check(account: Account): Account = {

    val urlValidator = RunningMode.current match {
      case RunningMode.Development => new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS)
      case RunningMode.Production  => new UrlValidator
    }

    if (account.homePage.isEmpty || account.name.isEmpty)
      throw new IllegalArgumentException("Account homepage or Account.name is empty")

    if (!urlValidator.isValid(account.homePage))
      throw new IllegalArgumentException("Account homePage: incorrect URI")

    account
  }
}
