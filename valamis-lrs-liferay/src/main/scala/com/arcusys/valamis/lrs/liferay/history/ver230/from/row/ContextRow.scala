package com.arcusys.valamis.lrs.liferay.history.ver230.from.row

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
case class ContextRow(key: Long,
                      registration: Option[String],
                      instructorId: Option[Long],
                      teamId: Option[Long],
                      contextActivitiesId: Option[Long],
                      revision: Option[String],
                      platform: Option[String],
                      language: Option[String],
                      statement: Option[String],
                      extensions: Option[String])