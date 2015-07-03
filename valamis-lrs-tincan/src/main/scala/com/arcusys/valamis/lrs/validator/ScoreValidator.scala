package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ScoreValidator {
  def checkNotNull(jValue: JValue) = {
    jValue \ scaled notNull

    jValue \ raw    notNull

    jValue \ min    notNull

    jValue \ max    notNull
  }
}
