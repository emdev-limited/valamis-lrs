package com.arcusys.valamis.lrs.serializer
import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.validator.ActorValidator
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import Constants.Tincan.Field._
/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */
class AgentSerializer(formatType: SerializeFormat) extends CustomSerializer[Agent](format => ( {
  case jObject: JObject =>
    implicit val f = format

    ActorValidator checkNotNull jObject

    val a = if (jObject.\(account).children.nonEmpty) {
      jObject.\(account).extract[Account].?
    } else None

    Agent(
      jObject.\(name)       .extractOpt[String].trimAll(),
      jObject.\(mBox)       .extractOpt[String].trimAll(),
      jObject.\(mBoxSha1Sum).extractOpt[String].trimAll(),
      jObject.\(openId)     .extractOpt[String].trimAll(),
      a
    )
}, {
  case agent: Agent => {
    implicit val formats: Formats = DefaultFormats + ShortTypeHints(List(classOf[Agent]))

    val result = if (formatType.Type == FormatType.Ids) {
      val field: JField = if (agent.mBox.isDefined)
        JField(mBox, JString(agent.mBox.get))
      else if (agent.mBoxSha1Sum.isDefined)
        JField(mBoxSha1Sum, JString(agent.mBoxSha1Sum.get))
      else if (agent.openId.isDefined)
        JField(openId, JString(agent.openId.get))
      else if (agent.account.isDefined)
        JField(account, Extraction.decompose(agent.account.get))
      else
        JField(account, null)

      JObject(JField("jsonClass", JString(StatementObjectType.Agent.toString.capitalize)), field)
    } else render(Extraction.decompose(agent)
      .removeField(isStoredId))
    result.transformField(fieldTransformer)
  }
})) {
  def this() = this(SerializeFormat())
}