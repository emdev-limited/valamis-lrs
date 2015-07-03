package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.liferay.exception.NotFoundException
import com.arcusys.valamis.lrs.liferay.servlet
import com.arcusys.valamis.lrs.liferay.servlet.request.TincanActivityRequest
import com.google.inject.{Inject, Injector, Singleton}

/**
 * Created by iliyatryapitsin on 25/12/14.
 */
@Singleton
class ActivityServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  /**
   * Loads the complete Activity Object specified.
   * @param request Required activityId
   * @param response Returns: 200 OK, Content
   */
  override def doGet(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanActivityRequest]({ model =>

    if(model.hasActivityName)
      lrs.getActivities(model.activityName)
    else
      lrs.getActivity(model.activityId) match {
        case Some(activity) => activity
        case None => throw new NotFoundException("Activity")
      }
  }, request, response)
}
