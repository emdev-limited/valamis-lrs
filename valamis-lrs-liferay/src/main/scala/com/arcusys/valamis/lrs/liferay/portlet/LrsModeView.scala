package com.arcusys.valamis.lrs.liferay.portlet

import javax.portlet._

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.liferay.{LrsModeLocator, LrsModule}
import com.arcusys.valamis.lrs.utils.RunningMode
import com.google.inject._
import html.apps.html._

/**
 * Created by Iliya Tryapitsin on 21.04.15.
 */
class LrsModeView extends GenericPortlet with LrsModeLocator {

  lazy val injector        = Guice.createInjector(LrsModule)
  lazy val securityManager = injector.getInstance(Key.get(classOf[SecurityManager]))

  private def getAction(r: PortletRequest) = {
    val a = r.getParameter(Action.Name)
    if(a == null || a.isEmpty) None
    else (Action withName a) toOption
  }

  override def processAction(request: ActionRequest, response: ActionResponse): Unit = {
    val act  = getAction(request)

    act match {

      case Some(Action.LrsModeChanged) =>
        val mode = request getParameter "currentMode"
        saveLrsModeSettings(mode)

        // apply selected mode
        RunningMode setCurrent mode

      case _ =>
    }
  }

  override def doView(request: RenderRequest, response: RenderResponse) {

    val mode = RunningMode.current
    val clientApiListView = lrsMode(
      new LrsModeViewModel(mode)(request, response)
    )
    response.getWriter write (index(clientApiListView) toString)
  }
}