package com.arcusys.valamis.lrs.liferay.portlet

import com.arcusys.valamis.lrs._
import javax.portlet.{RenderResponse, RenderRequest}

/**
  * Created by iliyatryapitsin on 12/11/15.
  */
class LrsModeViewModel(currentMode: RunningMode.Type)
                      (implicit request: RenderRequest,
                       response: RenderResponse) extends BasePortletView(request, response) {

  def isSelectedCurrentMode(tpe: RunningMode.Type) = if (currentMode == tpe) "selected" else ""
}