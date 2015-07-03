package com.arcusys.valamis.lrs.serializer

import com.arcusys.valamis.lrs.tincan._
import com.arcusys.valamis.lrs.validator._
import org.json4s.{JField, JObject, JString, JValue}
import org.json4s._
import Constants.Tincan.Field._

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

class ActivitySerializer(formatType: SerializeFormat) extends CustomSerializer[Activity](format => ( {
  case jValue: JValue =>
    implicit val formats: Formats = activitySerializer(formatType)

    jValue \ id notNull

    val jDefinition = jValue \ definition

    ActivityValidator checkNotNull jDefinition

    Activity(
      jValue      .\ (id)         .extract    [String],
      jDefinition .\ (name)       .extractOpt [LanguageMap],
      jDefinition .\ (description).extractOpt [LanguageMap],
      jDefinition .\ (`type`)     .extractOpt [String],
      jDefinition .\ (moreInfo)   .extractOpt [String],
      jDefinition .\ (interactionType).extractOpt[String].map(InteractionType.withName),
      jDefinition .\ (correctResponsesPattern).extract[Seq[String]].map { str => str.trimAll() },
      jDefinition .\ (choices)    .extract    [Seq[InteractionComponent]],
      jDefinition .\ (scale)      .extract    [Seq[InteractionComponent]],
      jDefinition .\ (source)     .extract    [Seq[InteractionComponent]],
      jDefinition .\ (target)     .extract    [Seq[InteractionComponent]],
      jDefinition .\ (steps)      .extract    [Seq[InteractionComponent]],
      jDefinition .\ (extensions) .extractOpt [ExtensionMap]
    )
}, {
  case activity: Activity =>
    implicit val formats: Formats = activitySerializer(formatType)

    val objectTypeField = JField(ObjectType, JString(StatementObjectType.activity.toString.capitalize))
    var n = activity.name.getOrElse(Map())
    var desc = activity.description.getOrElse(Map())

    def getJObject = JObject(
      objectTypeField,
      JField(id, JString(activity.id)),
      JField(definition, JObject(
        JField(name,        Extraction.decompose(activity.name)),
        JField(description, Extraction.decompose(activity.description)),
        JField(`type`,      Extraction.decompose(activity.theType)),
        JField(interactionType, Extraction.decompose(activity.interactionType)),
        JField(moreInfo,        Extraction.decompose(activity.moreInfo)),
        JField(correctResponsesPattern, Extraction.decompose(activity.correctResponsesPattern)),
        JField(choices,     Extraction.decompose(activity.choices)),
        JField(scale,       Extraction.decompose(activity.scale)),
        JField(source,      Extraction.decompose(activity.source)),
        JField(target,      Extraction.decompose(activity.target)),
        JField(steps,       Extraction.decompose(activity.steps)),
        JField(extensions,  Extraction.decompose(activity.extensions)))
      )
    )

    formatType.Type match {
      case FormatType.Ids => JObject(
        objectTypeField,
        JField(id, JString(activity.id))
      )

      case FormatType.Exact => getJObject

      case FormatType.Canonical => {

        if (! formatType.Lang.isEmpty) {
          val languages = formatType.Lang.split(Array(',', ';'))
          n = n.filter(l1 => languages.exists(l2 => l2.contains(l1._1))).take(1)
          desc = desc.filter(l1 => languages.exists(l2 => l2.contains(l1._1))).take(1)
        }
        getJObject
      }
    }
})) {
  def this() = this(SerializeFormat())
}
