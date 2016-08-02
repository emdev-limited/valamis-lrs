package com.arcusys.valamis.lrs.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 24/03/15.
 */
case class ContextActivityRow(id: Long,
                              grouping: Option[String],
                              category: Option[String],
                              other: Option[String],
                              parent: Option[String])