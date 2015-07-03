package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Constants
import org.json4s.JValue
import Constants.Tincan.Field._

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ActivityValidator {
  def checkNotNull(jValue: JValue) = {
    jValue notNull

    jValue \ name         notNull

    jValue \ description  notNull

    jValue \ `type`       notNull

    jValue \ moreInfo     notNull

    jValue \ interactionType          notNull

    jValue \ correctResponsesPattern  notNull

    jValue \ choices      notNull

    jValue \ scale        notNull

    jValue \ source       notNull

    jValue \ target       notNull

    jValue \ steps        notNull

    jValue \ extensions   notNull
  }
}
