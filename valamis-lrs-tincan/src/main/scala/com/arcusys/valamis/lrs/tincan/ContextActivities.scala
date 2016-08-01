package com.arcusys.valamis.lrs.tincan

/**
 * Traditional e-learning has included structures for interactions or assessments.
 * As a way to allow these practices and structures to extend Experience API's utility,
 * this specification includes built-in definitions for interactions, which borrows from the SCORM 2004 4th Edition Data Model.
 * @param parent
 * @param grouping
 * @param category
 * @param other
 */
case class ContextActivities(parent:   Seq[ActivityReference] = Seq(),
                             grouping: Seq[ActivityReference] = Seq(),
                             category: Seq[ActivityReference] = Seq(),
                             other:    Seq[ActivityReference] = Seq()) {

  lazy val allIds =
    categoryIds ++
    groupingIds ++
    otherIds    ++
    parentIds

  lazy val categoryIds = category.map { x => x.id }
  lazy val groupingIds = grouping.map { x => x.id }
  lazy val otherIds    = other.map    { x => x.id }
  lazy val parentIds   = parent.map   { x => x.id }



  override def toString =
    s"""
       |ContextActivities instance
       |parent          = $parent
       |grouping        = $grouping
       |category        = $category
       |other           = $other
     """.stripMargin
}
