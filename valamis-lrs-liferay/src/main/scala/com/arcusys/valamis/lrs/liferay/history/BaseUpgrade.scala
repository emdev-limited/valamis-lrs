package com.arcusys.valamis.lrs.liferay.history

import com.liferay.portal.kernel.log.LogFactoryUtil
import scala.util._

class BaseUpgrade {
  val logger = LogFactoryUtil.getLog(getClass)

  protected def tryAction(action: => Unit) = Try { action } match {
    case Success(_)  =>
    case Failure(ex) => logger.info(s"Can not migrate data: ${ex.getStackTrace}")
  }

}
