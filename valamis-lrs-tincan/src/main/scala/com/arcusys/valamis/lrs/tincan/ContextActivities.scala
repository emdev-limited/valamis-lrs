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
// TODO Change to Seq
case class ContextActivities(parent:   Set[ActivityReference],
                             grouping: Set[ActivityReference],
                             category: Set[ActivityReference],
                             other:    Set[ActivityReference]) {

  override def toString =
    s"""
       |ContextActivities instance
       |parent          = $parent
       |grouping        = $grouping
       |category        = $category
       |other           = $other
     """.stripMargin
}
