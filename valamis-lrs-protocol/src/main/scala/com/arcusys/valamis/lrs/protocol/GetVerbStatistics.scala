package com.arcusys.valamis.lrs.protocol

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetVerbStatistics(since: Option[DateTime] = None) extends Message
