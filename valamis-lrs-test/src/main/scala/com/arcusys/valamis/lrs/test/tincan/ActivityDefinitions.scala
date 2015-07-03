package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class ActivityDefinition(`type`: Option[String] = None,
                              name: Option[Map[String, String]] = None,
                              description: Option[Map[String, String]] = None,
                              moreInfo: Option[String] = None,
                              extensions: Option[Map[String, Any]] = None,
                              interactionType: Option[String] = None,
                              correctResponsesPattern: Option[Seq[String]] = None,
                              choices: Option[Map[String, Object]] = None,
                              source: Option[Map[String, Object]] = None,
                              scale: Option[Map[String, Object]] = None,
                              steps: Option[Map[String, Object]] = None,
                              target: Option[Map[String, Object]] = None)

object ActivityDefinitions {
  private val name = Some(Map("en-US" -> "test"))
  private val description = Some(Map("en-US" -> "test"))
  private val `type` = Some("http://id.tincanapi.com/activitytype/unit-test")
  private val moreInfo = Some("https://github.com/adlnet/xAPI_LRS_Test")

  val empty         = Some(ActivityDefinition())
  val typical       = Some(ActivityDefinition())
  val nameOnly      = Some(ActivityDefinition(name = name))
  val descriptionOnly       = Some(ActivityDefinition(description = description))
  val typeOnly              = Some(ActivityDefinition(`type` = `type`))
  val moreInfoOnly          = Some(ActivityDefinition(moreInfo = moreInfo))
  val extensionsOnly        = Some(ActivityDefinition(extensions = Extensions("multiplePairs")))
  val emptyExtensionsOnly   = Some(ActivityDefinition(extensions = Extensions("empty")))
  val allProperties         = Some(ActivityDefinition(name = name, description = description, `type` = `type`, moreInfo = moreInfo, extensions = Extensions("typical")))
  val typeNameDescriptionMoreInfo = Some(ActivityDefinition(name = name, description = description, `type` = `type`, moreInfo = moreInfo))
  val trueFalse             = Some(ActivityDefinition(interactionType = Some("true-false")))
  val fillIn                = Some(ActivityDefinition(interactionType = Some("fill-in")))
  val numeric               = Some(ActivityDefinition(interactionType = Some("numeric")))
  val other                 = Some(ActivityDefinition(interactionType = Some("other")))
  val otherWithCorrectResponsesPattern = Some(ActivityDefinition(interactionType = Some("other"), correctResponsesPattern = Some(Seq("test"))))
  val choice        = Some(ActivityDefinition(interactionType = Some("choice"), choices = Some(InteractionComponents.typical)))
  val sequencing    = Some(ActivityDefinition(interactionType = Some("sequencing"), choices = Some(InteractionComponents.typical)))
  val likert        = Some(ActivityDefinition(interactionType = Some("likert"), scale = Some(InteractionComponents.typical)))
  val matching      = Some(ActivityDefinition(interactionType = Some("matching"), source = Some(InteractionComponents.typical), target = Some(InteractionComponents.typical)))
  val performance   = Some(ActivityDefinition(interactionType = Some("performance"), steps = Some(InteractionComponents.typical)))
  val forQuery      = Some(ActivityDefinition(name = Some(Map("en-US" -> "for query"))))
}
