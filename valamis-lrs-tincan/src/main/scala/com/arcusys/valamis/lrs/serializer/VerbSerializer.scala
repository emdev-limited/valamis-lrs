package com.arcusys.valamis.lrs.serializer

import java.net.{URL, URI}

import com.arcusys.valamis.lrs.exception.VerbInvalidException
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.util.IRI
import com.arcusys.valamis.lrs.validator.VerbValidator
import org.apache.commons.lang.LocaleUtils
import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, CustomSerializer, Extraction}
import Constants.Tincan.Field._
import scala.util.{Failure, Success, Try}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object VerbSerializer extends CustomSerializer[Verb](format => ( {
  case jValue: JValue =>
    implicit val f = DefaultFormats

    VerbValidator checkNotNull jValue

    val url = jValue.\(id).extract[String]

    val languages = jValue \ display match {
      case JNothing   => LanguageMap()
      case v: JValue  => v.extract[LanguageMap]
    }

    VerbValidator checkRequirements Verb(url, languages)
}, {
  case verb: Verb =>
    render(Extraction.decompose(verb)(DefaultFormats))
}))
