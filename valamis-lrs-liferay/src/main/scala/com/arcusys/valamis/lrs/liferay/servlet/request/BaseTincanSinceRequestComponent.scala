package com.arcusys.valamis.lrs.liferay.servlet.request

import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 29/12/14.
 */

trait BaseTincanSinceRequestComponent {
  r: BaseLrsRequest =>

  import BaseTincanSinceRequestComponent._

  def since = optional(Since) map (x => new DateTime(x))
}

object BaseTincanSinceRequestComponent {
  val Since = "since"
}
