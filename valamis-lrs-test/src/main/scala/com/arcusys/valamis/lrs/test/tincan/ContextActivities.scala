package com.arcusys.valamis.lrs.test.tincan

/**
 * Created by Iliya Tryapitsin on 13/02/15.
 */

case class ContextActivity(category: Option[Seq[Activity]] = None,
                           parent: Option[Seq[Activity]] = None,
                           other: Option[Seq[Activity]] = None,
                           grouping: Option[Seq[Activity]] = None)

object ContextActivities {
  val empty = Some(ContextActivity())
  val typical = Some(ContextActivity())
  val categoryOnly = Some(ContextActivity(category = Some(Seq(Activities.typical.get))))
  val parentOnly = Some(ContextActivity(parent = Some(Seq(Activities.typical.get))))
  val otherOnly = Some(ContextActivity(other = Some(Seq(Activities.typical.get))))
  val groupingOnly = Some(ContextActivity(grouping = Some(Seq(Activities.typical.get))))

  val allPropertiesEmpty = Some(ContextActivity(
    category = Some(Seq()),
    parent = Some(Seq()),
    other = Some(Seq()),
    grouping = Some(Seq())))

  val allProperties = Some(ContextActivity(
    category = Some(Seq(Activities.typical.get)),
    parent = Some(Seq(Activities.typical.get)),
    other = Some(Seq(Activities.typical.get)),
    grouping = Some(Seq(Activities.typical.get))))
}
