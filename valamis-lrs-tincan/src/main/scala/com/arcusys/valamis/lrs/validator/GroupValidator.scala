package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Constants
import org.json4s.JsonAST.JValue
import Constants.Tincan.Field._
/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
object GroupValidator {
  def checkNotNull(jValue: JValue) = {
    ActorValidator checkNotNull jValue

    jValue \ member notNull
  }
}
