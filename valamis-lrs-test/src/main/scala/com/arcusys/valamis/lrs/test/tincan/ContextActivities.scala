package com.arcusys.valamis.lrs.test.tincan

import com.arcusys.valamis.lrs.utils._
  
/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class ContextActivity(category: Option[Seq[Activity]] = None,
                           parent: Option[Seq[Activity]] = None,
                           other: Option[Seq[Activity]] = None,
                           grouping: Option[Seq[Activity]] = None)

object ContextActivities {
  val empty = ContextActivity() ?
  val typical = ContextActivity() ?
  val categoryOnly = ContextActivity(category = Seq(Activities.typical.get)?)?
  val parentOnly = ContextActivity(parent = Seq(Activities.typical.get)?)?
  val otherOnly = ContextActivity(other = Seq(Activities.typical.get)?)?
  val groupingOnly = ContextActivity(grouping = Seq(Activities.typical.get)?)?

  val allPropertiesEmpty = ContextActivity(
    category = Seq()?,
    parent = Seq()?,
    other = Seq()?,
    grouping = Seq()?)?

  val allProperties = ContextActivity(
    category = Seq(Activities.typical.get)?,
    parent = Seq(Activities.typical.get)?,
    other = Seq(Activities.typical.get)?,
    grouping = Seq(Activities.typical.get)?)?
}
