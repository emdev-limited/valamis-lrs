package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class Activity(id: Option[String] = None,
                    objectType: Option[String] = None,
                    definition: Option[ActivityDefinition] = None) extends StatementObj

object Activities {
  private val id = Some("http://tincanapi.com/conformancetest/activityid")
  private val objectType = Some("Activity")

  val typical         = Some(Activity(id, objectType))
  val allProperties   = Some(Activity(id, objectType, ActivityDefinitions.allProperties))
  val noExtensions    = Some(Activity(id, objectType, ActivityDefinitions.typeNameDescriptionMoreInfo))

  object Good {
    val `should pass activity minimal` = Some(Activity(id))
    val `should pass activity typical` = typical
    val `activity with all properties` = allProperties
    val `activity can be with typical activity definitions` = Some(Activity(id, None, ActivityDefinitions.typical))
  }
}
