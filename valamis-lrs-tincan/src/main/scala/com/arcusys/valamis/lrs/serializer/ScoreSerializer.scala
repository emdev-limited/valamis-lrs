package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.Score
import com.arcusys.valamis.lrs.validator.ScoreValidator
import org.json4s.JsonAST.{JDouble, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{DefaultFormats, CustomSerializer, Extraction}

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
object ScoreSerializer extends CustomSerializer[Score](implicit format => ( {
  case jValue: JValue =>
    ScoreValidator checkNotNull jValue
    Score(
      jValue.\(scaled).extractOpt[String].map(s => s.toFloat),
      jValue.\(raw)   .extractOpt[String].map(s => s.toFloat),
      jValue.\(min)   .extractOpt[String].map(s => s.toFloat),
      jValue.\(max)   .extractOpt[String].map(s => s.toFloat)
    )
}, {
  case score: Score => render(Extraction.decompose(score)(DefaultFormats))
}))