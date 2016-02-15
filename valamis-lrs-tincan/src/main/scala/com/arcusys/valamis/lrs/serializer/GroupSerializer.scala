package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.tincan.Constants.Tincan.Field._
import com.arcusys.valamis.lrs.tincan.{FormatType, _}
import com.arcusys.valamis.lrs.validator.GroupValidator
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
class GroupSerializer(formatType: SerializeFormat) extends CustomSerializer[Group](format => ( {
  case jObject: JObject =>
    implicit val jsonFormats = groupSerializers(formatType)

    GroupValidator checkNotNull jObject

    val a = if (jObject.\(account).children.nonEmpty) {
      jObject.\(account).extract[Account].?
    } else None

    Group(
      jObject .\(name)       .extractOpt[String],
      jObject .\(member)     .extractOpt[Seq[Actor]].map { x => x.map { _.asInstanceOf[Agent] } },
      jObject .\(mBox)       .extractOpt[String].trimAll(),
      jObject .\(mBoxSha1Sum).extractOpt[String].trimAll(),
      jObject .\(openId)     .extractOpt[String].trimAll(),
      a
    )

}, {
  case group: Group =>
    implicit val jsonFormats: Formats = DefaultFormats + 
      ShortTypeHints(List(classOf[Group])) +
      new AgentSerializer(formatType)

    val result = if (formatType.Type == FormatType.Ids) {
      val field: JField = if (group.mBox.isDefined)
        JField(mBox, JString(group.mBox.get))
      else if (group.mBoxSha1Sum.isDefined)
        JField(mBoxSha1Sum, JString(group.mBoxSha1Sum.get))
      else if (group.openId.isDefined)
        JField(openId, JString(group.openId.get))
      else
        JField(account, Extraction.decompose(group.account.get))

      if (group.member.isDefined)
        JObject(JField(member, Extraction.decompose(group.member.get)), field)

      JObject(JField("jsonClass", JString(StatementObjectType.Group.toString.capitalize)), field)
    } else
      render(Extraction.decompose(group).removeField(field => field._1.equalsIgnoreCase("storedId")))
    result.transformField({
      case JField("jsonClass", jValue: JString) => JField(ObjectType, jValue)
      case JField("mBox", jValue: JString) => JField(mBox, jValue)
      case JField("mBoxSha1Sum", jValue: JString) => JField(mBoxSha1Sum, jValue)
      case JField("openId", jValue: JString) => JField(openId, jValue)
    })
})) {
  def this() = this(SerializeFormat())
}
