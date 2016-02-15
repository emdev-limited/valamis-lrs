package com.arcusys.valamis.lrs.liferay.servlet.oauth

import java.io.OutputStream
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.google.inject.{Inject, Injector, Singleton}
import net.oauth.server.OAuthServlet
import net.oauth.{OAuth, OAuthAccessor}


@Singleton
class RequestTokenServlet @Inject()(inj: Injector) extends BaseAuthServlet(inj) {


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
      val requestMessage = getMessage(request)
      val consumer = getConsumer(requestMessage, checkScope = true)
      val accessor = new OAuthAccessor(consumer)
      validator.validateMessage(requestMessage, accessor)

      generateRequestToken(accessor)
      response.setContentType("text/plain")
      val out: OutputStream = response.getOutputStream
      OAuth.formEncode(OAuth.newList(OAuthToken,               accessor.requestToken,
                                     OAuthTokenSecret,         accessor.tokenSecret,
                                     OAuthCallbackConfirmed,   checkCallback(consumer.callbackURL).toString), out)
      out.close()
      out.flush()
    }
    catch {
      case e: Exception =>
        handleException(e, request, response)
    }
  }

}
