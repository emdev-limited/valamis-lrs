package com.arcusys.valamis.lrs.protocol

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetVerbsWithActivities(filter:  Option[String] = None,
                                  limit:   Int = 100,
                                  offset:  Int = 0,
                                  ascSort: Boolean = true) extends Message
