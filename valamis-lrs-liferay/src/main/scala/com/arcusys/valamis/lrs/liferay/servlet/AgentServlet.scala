package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.liferay.servlet
import com.arcusys.valamis.lrs.liferay.servlet.request.TincanAgentProfileRequest
import com.google.inject.{Inject, Injector, Singleton}

/**
 * Created by iliyatryapitsin on 25/12/14.
 */

@Singleton
class AgentServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](

    model => lrs.getPerson(model.agent), request, response)
}