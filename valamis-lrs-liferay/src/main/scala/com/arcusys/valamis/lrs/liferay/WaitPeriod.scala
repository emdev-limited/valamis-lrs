package com.arcusys.valamis.lrs.liferay

import akka.util.Timeout
import scala.concurrent.duration._

/**
 * Created by Iliya Tryapitsin on 27.04.15.
 */
object WaitPeriod {
  implicit val duration = 10 seconds
  implicit val timeout = Timeout(duration)
}
