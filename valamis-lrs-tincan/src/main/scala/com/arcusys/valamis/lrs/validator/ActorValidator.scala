package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.tincan.{Actor, Constants}
import org.apache.commons.validator.routines.{EmailValidator, UrlValidator}
import org.json4s.JsonAST.JValue
import Constants.Tincan.Field._
/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object ActorValidator {

  val urlValidator   = UrlValidator.getInstance()
  val emailValidator = EmailValidator.getInstance()
  val mailTo         = "mailto:"

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
    actor.mBox   whenDefined checkMBox
    actor.openId whenDefined checkUrl
  }

  private def checkMBox(email: String) = {
    email contains mailTo whenFalse { r =>
      throw new IllegalArgumentException("Actor mbox: should contains 'mailto:' prefix")
    }

    email removeAll mailTo afterThat checkEmail
  }

  private def checkEmail (email: String) = emailValidator isValid email whenFalse { r =>
    throw new IllegalArgumentException("Actor mbox: incorrect email address")
  }

  private def checkUrl (url: String) = urlValidator isValid url whenFalse { r =>
    throw new IllegalArgumentException("Actor openid: incorrect URI")
  }

  private def checkIfEmpty(actor: Actor) = if (
    actor.name.isEmpty &&
    actor.mBox.isEmpty &&
    actor.mBoxSha1Sum.isEmpty &&
    actor.openId.isEmpty &&
    actor.account.isEmpty)
    throw new IllegalArgumentException
}




