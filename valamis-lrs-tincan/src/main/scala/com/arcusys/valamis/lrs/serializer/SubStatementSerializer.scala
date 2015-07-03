package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan.{Constants, StatementObjectType, SubStatement}
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.{CustomSerializer, Extraction}

/**
 * Created by Iliya Tryapitsin on 06/03/15.
 */
class SubStatementSerializer(formatType: SerializeFormat) extends CustomSerializer[SubStatement](format => ( {
  case jObject: JObject =>
    implicit val f = subStatementSerializers(formatType)

    jObject
      .transformField(fieldTransformer)
      .extract[SubStatement]
}, {
  case subStatement: SubStatement =>
    implicit val f = subStatementSerializers(formatType)

    JObject(
      JField(Constants.Tincan.Field.ObjectType, JString(StatementObjectType.subStatement.toString.capitalize)),
      JField(Constants.Tincan.Actor, Extraction.decompose(subStatement.actor)),
      JField(Constants.Tincan.Verb, Extraction.decompose(subStatement.verb)),
      JField(Constants.Tincan.Field.obj, Extraction.decompose(subStatement.obj))
    )
})) {
  def this() = this(SerializeFormat())
}
