package com.arcusys.valamis.lrs.history.ver230.from.row

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 18/03/15.
 */

case class StatementRow(key: Long,
                        id: Option[String],
                        actorKey: Option[Long],
                        verbId: Option[String],
                        verbDisplay: Option[String],
                        objType: Option[String],
                        objKey: Option[Long],
                        resultKey: Option[Long],
                        contextKey: Option[Long],
                        timestamp: Option[DateTime],
                        stored: Option[DateTime],
                        authorityKey: Option[Long],
                        version: Option[String])
