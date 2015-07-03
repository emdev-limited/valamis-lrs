package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http._

import com.arcusys.valamis.lrs.liferay._

/**
 * Created by Iliya Tryapitsin on 29.04.15.
 */
abstract class BaseServlet  extends HttpServlet {
  override def doOptions(request: HttpServletRequest,
                         response: HttpServletResponse): Unit = {

    response.setHeader(AccessControlAllowMethods , s"$Head,$Get,$Post,$Put,$Delete")
    response.setHeader(AccessControlAllowHeaders , s"$ContentType,$ContentLength,$Authorization")
    response.setHeader(AccessControlExposeHeaders, s"$ETag,$LastModified,$CacheControl,$ContentType,$ContentLength,$WwwAuthenticate")

    setHeaders(response)
  }

  protected def setHeaders(response: HttpServletResponse): Unit = {

    response.setHeader(CacheControl, s"$MustRevalidate,$NoCache,$NoStore")
    response.setHeader(Expires     , Never)
    response.setHeader(AccessControlAllowOrigin, All)
  }
}
