package com.arcusys.valamis.lrs.liferay.servlet.valamis

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.jdbc.{ExecutionContext, SimpleExecutionContext}
import com.arcusys.valamis.lrs.liferay.servlet.BaseLrsServlet
import com.arcusys.valamis.lrs.liferay.servlet.request.valamis.{ValamisStatementsRequest, ValamisScaleRequest}

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

//import com.arcusys.valamis.lrs.message.GetActivityScaled
import com.google.inject._

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
@Singleton
class StatementExtServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  override def doGet(request : HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[ValamisStatementsRequest]({ model =>

    model.action match {
      case model.GetCountByParams =>
        reporter.findStatementsCount(model.agent, model.verbs)

      case model.FindMinDate      =>
        reporter.findMinDate(model.agent, model.verbs, model.activityIds, model.since)

    }
  }, request, response)

  override val ServletName: String = "StatementExt"
}