package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.tincan.Context
import org.json4s.JsonAST.JValue

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object ContextValidator {
  import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
  import com.arcusys.valamis.lrs.tincan.Constants.Tincan._

  def checkNotNull(jValue: JValue) = {
    jValue \ registration   notNull

    jValue \ instructor     notNull

    jValue \ team           notNull

    jValue \ ContextActivities   notNull

    jValue \ revision      notNull

    jValue \ platform      notNull

    jValue \ language      notNull

    jValue \ Statement     notNull

    jValue \ extensions    notNull
  }

  def checkRequirements(cntx: Context) = cntx.language match {
    case Some(v) if  v.isEmpty  => throw new IllegalArgumentException
    case Some(v) if !v.isEmpty  => cntx
    case None                   => cntx
  }
}
