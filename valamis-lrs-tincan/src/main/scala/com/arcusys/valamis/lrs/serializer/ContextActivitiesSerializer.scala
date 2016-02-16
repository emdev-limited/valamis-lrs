package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan._
import org.json4s.JsonAST.{JObject, JArray, JNothing, JValue}
import org.json4s.jackson.JsonMethods._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction, Formats}

class ContextActivitiesSerializer extends CustomSerializer[ContextActivities](format => ( {
  case jValue: JValue =>
    implicit val jsonFormats: Formats = DefaultFormats +
      new StatementObjectSerializer() +
      new EnumNameIgnoreCaseSerializer(StatementObjectType)

    val parent = jValue \ "parent" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val grouping = jValue \ "grouping" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val category = jValue \ "category" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }
    val other = jValue \ "other" match {
      case JNothing => Seq[ActivityReference]()
      case value: JObject => Seq(value.extract[ActivityReference])
      case value: JArray => value.extract[Seq[ActivityReference]]
    }

    ContextActivities(parent, grouping, category, other)
}, {
  case contextActivities: ContextActivities =>
    implicit val jsonFormats: Formats = DefaultFormats +
      new StatementObjectSerializer() +
      new EnumNameIgnoreCaseSerializer(StatementObjectType)

    render(Extraction.decompose(contextActivities)(jsonFormats))
}))
