package com.arcusys.valamis.lrs.liferay.servlet.valamis

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.liferay.servlet.BaseLrsServlet
import com.arcusys.valamis.lrs.liferay.servlet.request.valamis.ValamisScaleRequest
import com.google.inject.name.Names

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

//import com.arcusys.valamis.lrs.message.GetActivityScaled
import com.google.inject._

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
@Singleton
class ScaleServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[ValamisScaleRequest]({ model =>

    model.action match {
      case model.ActivityScale =>
        reporter.findMaxActivityScaled(model.agent, model.verb)
    }
  }, request, response)

  override val ServletName: String = "Scale"
}
