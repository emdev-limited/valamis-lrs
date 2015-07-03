package com.arcusys.valamis.lrs.liferay.portlet

import javax.portlet.{RenderResponse, RenderRequest}

/**
 * Created by Iliya Tryapitsin on 26.04.15.
 */
class BasePortletView(val request:  RenderRequest,
                      val response: RenderResponse){
  def getTagName(name: String) = response.getNamespace + name

  def actionUrl = response.createActionURL()

  def renderUrl = response.createRenderURL()
}
