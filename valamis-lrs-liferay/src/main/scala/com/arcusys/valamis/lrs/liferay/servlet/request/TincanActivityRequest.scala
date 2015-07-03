package com.arcusys.valamis.lrs.liferay.servlet.request

import javax.servlet.http.HttpServletRequest

/**
 * Created by iliyatryapitsin on 26/12/14.
 */

class TincanActivityRequest(r: HttpServletRequest) extends BaseLrsRequest(r) {

  import TincanActivityRequest._

  def activityName = require(ACTIVITY_NAME)

  def activityId = require(ACTIVITY_ID)

  def hasActivityName = has(ACTIVITY_NAME)
}

object TincanActivityRequest {
  val ACTIVITY_NAME = "activity"
  val ACTIVITY_ID = "activityId"
}
