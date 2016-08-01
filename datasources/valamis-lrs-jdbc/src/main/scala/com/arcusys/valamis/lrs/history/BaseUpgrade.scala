package com.arcusys.valamis.lrs.history

import com.arcusys.valamis.lrs.jdbc.Loggable

import scala.util._

class BaseUpgrade extends Loggable {

  protected def tryAction(action: => Unit) = Try { action } match {
    case Success(_)  =>
    case Failure(ex) => logger.info(s"Can not migrate data: ${ex.getStackTrace}")
  }

}
