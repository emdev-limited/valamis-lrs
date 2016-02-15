package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.validator.VerbValidator
import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object VerbSerializer extends CustomSerializer[Verb](format => ( {
  case jValue: JValue =>
    implicit val f = DefaultFormats

    VerbValidator checkNotNull jValue

    val url = jValue.\(Id).extract[String]

    val languages = jValue \ display match {
      case JNothing   => LanguageMap()
      case v: JValue  => v.extract[LanguageMap]
    }

    VerbValidator checkRequirements Verb(url, languages)
}, {
  case verb: Verb =>
    render(Extraction.decompose(verb)(DefaultFormats))
}))
