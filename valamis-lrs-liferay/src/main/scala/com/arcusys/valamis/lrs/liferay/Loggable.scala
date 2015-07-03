package com.arcusys.valamis.lrs.liferay

import com.liferay.portal.kernel.log.LogFactoryUtil

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
trait Loggable {
  val logger = LogFactoryUtil.getLog("com.arcusys")
}
