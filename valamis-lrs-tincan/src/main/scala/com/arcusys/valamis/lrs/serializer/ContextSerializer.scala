package com.arcusys.valamis.lrs.serializer

import java.util.UUID

import com.arcusys.valamis.lrs.tincan.Constants.Tincan
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.Constants.Tincan._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.validator.ContextValidator
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, Extraction}
/**
 * Created by Iliya Tryapitsin on 11/02/15.
 */
class ContextSerializer(formatType: SerializeFormat) extends CustomSerializer[Context](format => ( {
  case jValue: JValue => implicit val jsonFormats = contextSerializers(formatType)

    ContextValidator checkNotNull jValue

    Context(
      jValue .\ (registration)  .extractOpt[UUID],
      jValue .\ (instructor)    .extractOpt[Actor],
      jValue .\ (team)          .extractOpt[Group],
      jValue .\ (Tincan.ContextActivities)   .extractOpt[ContextActivities],
      jValue .\ (revision)      .extractOpt[String].trimAll(),
      jValue .\ (platform)      .extractOpt[String].trimAll(),
      jValue .\ (language)      .extractOpt[String].trimAll(),
      jValue .\ (Tincan.Statement)     .extractOpt[StatementReference],
      jValue .\ (extensions)    .extractOpt[ExtensionMap]
    )
}, {
  case c: Context => render(Extraction.decompose(c)(contextSerializers(formatType)))
})) {
  def this() = this(SerializeFormat())
}