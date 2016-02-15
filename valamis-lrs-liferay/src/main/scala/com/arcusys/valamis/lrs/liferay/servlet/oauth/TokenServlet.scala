package com.arcusys.valamis.lrs.liferay.servlet.oauth

import java.io.OutputStream
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.google.inject.{Inject, Injector, Singleton}
import net.oauth.server.OAuthServlet
import net.oauth.{OAuth, OAuthAccessor, OAuthMessage}


@Singleton
class TokenServlet @Inject()(inj: Injector) extends BaseAuthServlet(inj) {

  val TextPlain   = "text/plain"

  override def doPost(request:  HttpServletRequest,
                      response: HttpServletResponse) = {
    processRequest(request, response)
  }

  override def doGet(request:  HttpServletRequest,
                      response: HttpServletResponse) = {
    processRequest(request, response)
  }

  def processRequest(request: HttpServletRequest, response: HttpServletResponse) {
    try {
      val requestMessage: OAuthMessage = getMessage(request)
      val accessor: OAuthAccessor = getAccessor(requestMessage)
      validator.validateMessage(requestMessage, accessor)
      generateAccessToken(accessor)
      response.setContentType(TextPlain)
      val out: OutputStream = response.getOutputStream
      OAuth.formEncode(OAuth.newList(OAuthToken,       accessor.accessToken,
                                     OAuthTokenSecret, accessor.tokenSecret), out)
      out.close()
      out.flush()
    }
    catch {
      case e: Exception =>
        handleException(e, request, response)
    }
  }
}
