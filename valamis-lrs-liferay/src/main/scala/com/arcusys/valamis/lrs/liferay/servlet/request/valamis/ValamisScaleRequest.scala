package com.arcusys.valamis.lrs.liferay.servlet.request.valamis

import javax.servlet.http.HttpServletRequest
import com.arcusys.valamis.lrs.liferay.servlet.request._

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
class ValamisScaleRequest (r: HttpServletRequest)
  extends BaseLrsRequest(r)
  with BaseTincanAgentRequestComponent
  with ValamisActionRequestComponent {

  def verb = require(Verb)

  val Verb = "verb"
  val ActivityScale = "activity-scale"
}
