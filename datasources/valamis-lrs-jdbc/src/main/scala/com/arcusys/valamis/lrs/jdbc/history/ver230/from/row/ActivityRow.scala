package com.arcusys.valamis.lrs.jdbc.history.ver230.from.row

import com.arcusys.valamis.lrs.tincan._
import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.jdbc.history.Helper._

/**
 * Created by Iliya Tryapitsin on 18/03/15.
 */
case class ActivityRow(key: Long,
                       id: Option[String],
                       packageId: Option[Long],
                       objectType: Option[String],
                       name: Option[String],
                       description: Option[String],
                       theType: Option[String],
                       moreInfo: Option[String],
                       interactionType: Option[String],
                       correctResponsesPattern: Option[String],
                       choices: Option[String],
                       scale: Option[String],
                       source: Option[String],
                       target: Option[String],
                       steps: Option[String],
                       extensions: Option[String]) {

  def toModel = Activity(
    id = id.get,
    name = getLanguageMapOption(name),
    description = getLanguageMapOption(description),
    theType = theType,
    moreInfo = moreInfo,
    interactionType = interactionType.map { x => InteractionType.withName(x) },
    correctResponsesPattern = correctResponsesPattern.map {x => JsonHelper.fromJson[Seq[String]](x)}.getOrElse(Seq[String]()),
    choices = getInteractionComponent(choices),
    scale = getInteractionComponent(scale),
    source = getInteractionComponent(source),
    target = getInteractionComponent(target),
    steps = getInteractionComponent(steps),
    extensions = getExtensions(extensions)
  )
}
