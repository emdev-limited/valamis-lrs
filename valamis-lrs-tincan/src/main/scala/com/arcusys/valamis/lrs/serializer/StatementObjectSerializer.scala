package com.arcusys.valamis.lrs.serializer

import java.beans.Introspector

import com.arcusys.valamis.lrs.tincan._
import org.json4s.JsonAST.JObject
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
class StatementObjectSerializer(formatType: SerializeFormat) extends CustomSerializer[StatementObject](format => ({
  case jObject: JObject =>
    implicit val jsonFormats = statementObjectSerializers(formatType)

    val objectType = jObject.\(Constants.Tincan.Field.ObjectType).extractOpt[String] match {
      case None => Constants.Tincan.Activity
      case Some(value) => Introspector.decapitalize(value)
    }
    objectType match {
      case Constants.Tincan.Agent |
           Constants.Tincan.Group         => jObject.extract[Actor]
      case Constants.Tincan.StatementReference => jObject.extract[StatementReference]
      case Constants.Tincan.SubStatement  => jObject.extract[SubStatement]
      case Constants.Tincan.Activity      => jObject.extract[Activity]

      case e => throw new IllegalArgumentException("Illegal object type for a Statement Object " + e)
    }
}, {
  case a: Agent               => render(Extraction.decompose(a)(DefaultFormats + new ActorSerializer(formatType)))
  case g: Group               => render(Extraction.decompose(g)(DefaultFormats + new ActorSerializer(formatType)))
  case s: SubStatement        => render(Extraction.decompose(s)(DefaultFormats + new SubStatementSerializer(formatType)))
  case s: StatementReference  => render(Extraction.decompose(s)(DefaultFormats + StatementReferenceSerializer))
  case a: Activity            => render(Extraction.decompose(a)(DefaultFormats + new ActivitySerializer(formatType)))
})) {
  def this() = this(SerializeFormat())
}