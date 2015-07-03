package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.Constants.Tincan._
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ResultValidator {
  def checkNotNull(jValue: JValue) = {
    jValue \ Score      notNull

    jValue \ success    notNull

    jValue \ completion notNull

    jValue \ response   notNull

    jValue \ duration   notNull

    jValue \ extensions notNull
  }

}
