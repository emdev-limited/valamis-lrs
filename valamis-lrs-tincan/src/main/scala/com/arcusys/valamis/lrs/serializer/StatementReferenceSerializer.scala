package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan.StatementReference
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * Created by Iliya Tryapitsin on 06/03/15.
 */
object StatementReferenceSerializer extends CustomSerializer[StatementReference](format => ({
  case jObject: JObject =>
    implicit val formats = statementRefSerializers

    jObject.extract[StatementReference]
}, {
  case statementRef: StatementReference =>
    implicit val formats = statementRefSerializers

    render(
      Extraction.decompose(statementRef)
        .removeField(isStoredId)
        .transformField(fieldTransformer[StatementReference])
    )
}))
