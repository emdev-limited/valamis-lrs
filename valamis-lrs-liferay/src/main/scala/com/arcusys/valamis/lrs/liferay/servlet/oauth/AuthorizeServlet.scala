package com.arcusys.valamis.lrs.liferay.servlet.oauth

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import akka.pattern.Patterns
import com.arcusys.valamis.lrs.auth.Authentication
import com.arcusys.valamis.lrs.liferay.WaitPeriod._
import com.arcusys.valamis.lrs.liferay._
import com.liferay.portal.service.UserLocalServiceUtil
import com.liferay.portal.util.PortalUtil
import scala.concurrent.Await
import scala.util._
import com.google.inject.{Inject, Injector, Singleton}
import net.oauth.{OAuth, OAuthAccessor}
import net.oauth.server.OAuthServlet

@Singleton
class AuthorizeServlet @Inject()(inj: Injector) extends BaseAuthServlet(inj) {

  val Description = "description"
  val ConsDesc    = "CONS_DESC"
  val Callback    = "CALLBACK"
  val Token       = "TOKEN"
  val TextPlain   = "text/plain"
  val NoneStr     = "none"
  val UserId      = "userId"

  override def doGet(request:  HttpServletRequest,
                     response: HttpServletResponse) = Try {
    val requestMessage = OAuthServlet.getMessage(request, null)
    val accessor = getAuthorizeAccessor(requestMessage)
    if (accessor.getProperty(Authorized).asInstanceOf[Boolean]) {
      markAsAuthorized(accessor)
      returnToConsumer(request, response, accessor)
    }
    else sendToAuthorizePage(request, response, accessor)
  } match {
    case Success(_) =>
    case Failure(ex: Exception) => handleException(ex, request, response)
    case Failure(ex) =>
      logger.error(ex)
      throw ex
  }

  override def doPost(request:  HttpServletRequest,
                      response: HttpServletResponse) = Try {
    val requestMessage = OAuthServlet.getMessage(request, null)
    val accessor = getAuthorizeAccessor(requestMessage)
    markAsAuthorized(accessor)
    returnToConsumer(request, response, accessor)
  } match {
    case Success(_) =>
    case Failure(ex: Exception) => handleException(ex, request, response)
    case Failure(ex) =>
      logger.error(ex)
      throw ex
  }

  private def getUserByRequest(request: HttpServletRequest) =
    PortalUtil.getUser(request) match {
      case null =>
        val defaultCompanyId = PortalUtil.getDefaultCompanyId
        UserLocalServiceUtil.getDefaultUser(defaultCompanyId)
      case user => user
    }

  private def sendToAuthorizePage(request : HttpServletRequest,
                                  response: HttpServletResponse,
                                  accessor: OAuthAccessor) = {
    response.setStatus(MovedTemporarily)
    response.setHeader(Location, s"${request.getScheme}://${request.getServerName}:${request.getServerPort}/c/portal/login")
  }

  private def returnToConsumer(request: HttpServletRequest,
                               response: HttpServletResponse,
                               accessor: OAuthAccessor) = {
    val callback = authentication.GetCallback(accessor.requestToken)

    val verifier = accessor.getProperty(OAuthVerifier)

    callback match {

      case Some(v) =>
        val redirectUrl = OAuth.addParameters(v, OAuthToken, accessor.requestToken,
                                                        OAuthVerifier, s"$verifier")
        response.setStatus(MovedTemporarily)
        response.setHeader(Location, redirectUrl)

      case None =>
        response.setContentType(TextPlain)
        response.getWriter.println(
          s"You have successfully authorized. Below is the verification code for this authorization: <br> '$verifier' ")
        response.getWriter.close()
    }
  }
}
