package com.arcusys.valamis.lrs.protocol

import com.arcusys.valamis.lrs.tincan.Activity
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 07.08.15.
 */
case class GetMultipleDocument(activityId: Activity#Id,
                               since:      Option[DateTime] = None) extends Message
