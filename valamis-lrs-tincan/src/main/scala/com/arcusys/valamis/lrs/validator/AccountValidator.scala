package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Account
import org.apache.commons.validator.routines.{EmailValidator, UrlValidator}
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object AccountValidator {
  val urlValidator = UrlValidator.getInstance()
  val emailValidator = EmailValidator.getInstance()

  import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._

  def checkNotNull(jValue: JValue) = {
    jValue \ name     notNull

    jValue \ homePage notNull
  }

  def check(account: Account): Account = {
    if (account.homePage.isEmpty && account.name.isEmpty)
      throw new IllegalArgumentException("Account homepage and Account.name is empty")

    if (!UrlValidator.getInstance().isValid(account.homePage))
      throw new IllegalArgumentException("Account homePage: incorrect URI")

    account
  }
}
