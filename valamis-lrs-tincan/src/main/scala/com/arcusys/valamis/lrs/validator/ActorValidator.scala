package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.{Actor, Constants}
import org.apache.commons.validator.routines.{EmailValidator, UrlValidator}
import org.json4s.JsonAST.JValue
import Constants.Tincan.Field._
/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object ActorValidator {

  val urlValidator = UrlValidator.getInstance()
  val emailValidator = EmailValidator.getInstance()

  def checkNotNull(jValue: JValue) = {
    jValue \ ObjectType   notNull

    jValue \ mBox         notNull

    jValue \ mBoxSha1Sum  notNull

    jValue \ name         notNull

    jValue \ openId       notNull

    jValue \ account      notNull
  }

  def check(actor: Actor) = {
    checkIfEmpty(actor)
    checkMBox(actor.mBox)
    checkOpenId(actor.openId)
  }

  private def checkMBox(mBox: Option[String]) = mBox match {
    case Some(value) => {
      if (!value.contains("mailto:"))
        throw new IllegalArgumentException("Actor mbox: should contains 'mailto:' prefix")

      val email = value.replaceAll("mailto:", "")
      if (!emailValidator.isValid(email))
        throw new IllegalArgumentException("Actor mbox: incorrect email address")
    }
    case None =>
  }

  private def checkOpenId(openId: Option[String]) = openId match {
    case Some(value) => if (!urlValidator.isValid(value))
      throw new IllegalArgumentException("Actor openid: incorrect URI")

    case None =>
  }

  private def checkIfEmpty(actor: Actor) = if (
    actor.name.isEmpty &&
    actor.mBox.isEmpty &&
    actor.mBoxSha1Sum.isEmpty &&
    actor.openId.isEmpty &&
    actor.account.isEmpty)
    throw new IllegalArgumentException
}




