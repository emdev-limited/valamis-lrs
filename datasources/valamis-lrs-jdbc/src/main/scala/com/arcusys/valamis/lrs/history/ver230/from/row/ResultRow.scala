package com.arcusys.valamis.lrs.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
case class ResultRow(key: Long,
                     score: Option[String],
                     success: Option[String],
                     completion: Option[String],
                     response: Option[String],
                     extension: Option[String],
                     duration: Option[String])
