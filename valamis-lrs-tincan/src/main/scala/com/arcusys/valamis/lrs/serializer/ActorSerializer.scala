package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.exception.CanNotBeNullException
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan._
import org.json4s.JsonAST.{JNothing, JNull, JObject, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, Extraction, Formats}
/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
class ActorSerializer(formatType: SerializeFormat) extends CustomSerializer[Actor](format => ( {
  case jObject: JObject =>
    implicit val jsonFormats: Formats = actorSerializers(formatType)

    val objType = jObject \ ObjectType match {
      case JNothing       => Constants.Tincan.Agent
      case JNull          => throw new CanNotBeNullException("Actor objectType cant not be NULL")
      case value: JValue  => value.extract[String].toLowerCase
    }

    objType match {
      case Constants.Tincan.Agent |
           Constants.Empty        => jObject.extract[Agent]
      case Constants.Tincan.Group => jObject.extract[Group]
      case _                      => throw new IllegalArgumentException
    }
}, {
  case agent: Agent =>
    render(Extraction.decompose(agent)(actorSerializers(formatType)))

  case group: Group =>
    render(Extraction.decompose(group)(actorSerializers(formatType)))
})) {
  def this() = this(SerializeFormat())
}