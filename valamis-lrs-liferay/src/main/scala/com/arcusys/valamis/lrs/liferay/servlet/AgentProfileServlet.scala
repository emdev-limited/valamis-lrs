package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.liferay.exception.NotFoundException
import com.arcusys.valamis.lrs.liferay.servlet
import com.arcusys.valamis.lrs.liferay.servlet.request.TincanAgentProfileRequest
import com.google.inject.{Inject, Injector, Singleton}

/**
 * Created by Iliya Tryapitsin on 25/12/14.
 */

@Singleton
class AgentProfileServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  override def doGet(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](model =>
    if (model.hasProfileId) {
      lrs.getProfileContent(model.agent, model.profileId.get) match {
        case Some(document) => document.contents
        case None => throw new NotFoundException("Agent profile")
      }
    } else {
      lrs.getProfiles(model.agent, model.since)
    }, request, response)

  override def doPost(request: HttpServletRequest,
                      response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](model => {

    lrs.addOrUpdateDocument(model.agent, model.profileId.get, model.document)
    Unit
  }, request, response)

  override def doPut(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](model => {

    lrs.addOrUpdateDocument(model.agent, model.profileId.get, model.document)
    Unit
  }, request, response)

  override def doDelete(request: HttpServletRequest,
                        response: HttpServletResponse): Unit = jsonAction[TincanAgentProfileRequest](model => {

    lrs.deleteProfile(model.agent, model.profileId.get)
    Unit
  }, request, response)

  override val ServletName: String = "AgentProfile"
}
