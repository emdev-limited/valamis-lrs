package com.arcusys.valamis.lrs.validator

import com.arcusys.valamis.lrs.exception.VerbInvalidException
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.{LanguageMapValidator, Verb}
import com.arcusys.valamis.lrs.util.IRI
import org.json4s.JsonAST.JValue

import scala.util.{Success, Failure, Try}

/**
 * Created by Iliya Tryapitsin on 02/04/15.
 */
object VerbValidator {
  def checkNotNull(jValue: JValue) = {
    jValue \ id notNull

    jValue \ display notNull
  }

  def checkRequirements(v: Verb): Verb = {
    v.display.foreach { lang =>
      Try {
        LanguageMapValidator checkRequirements lang
      } match {
        case Failure(_) => throw new VerbInvalidException(v.id)
        case Success(_) =>
      }
    }

    Try(new IRI(v.id)) match {
      case Failure(_) => throw new VerbInvalidException(v.id)
      case Success(_) => v
    }
  }
}
