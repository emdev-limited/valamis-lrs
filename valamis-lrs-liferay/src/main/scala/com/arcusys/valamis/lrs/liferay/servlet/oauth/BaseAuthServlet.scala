package com.arcusys.valamis.lrs.liferay.servlet.oauth

import java.util.UUID
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import akka.actor.ActorSystem
import akka.pattern.Patterns
import com.arcusys.valamis.lrs.GuiceAkkaExtension
import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.liferay.Loggable
import com.arcusys.valamis.lrs.liferay.WaitPeriod._
import com.arcusys.valamis.lrs.liferay.servlet.BaseServlet
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.google.inject.Injector
import net.oauth._
import net.oauth.server.OAuthServlet

import scala.concurrent.Await
import scala.util.Try

abstract class BaseAuthServlet(inj: Injector) extends BaseServlet with Loggable {

  val authentication = inj.getInstance(classOf[Authentication])
//  val system              = inj.getInstance(classOf[ActorSystem])
//  val authenticationActor = system.actorOf(GuiceAkkaExtension(system).props(AuthenticationActor.name))
  val validator = new SimpleOAuthValidator
  val Name = "name"

  // OAuth Provider's methods
  def getConsumer(requestMessage: OAuthMessage, checkScope: Boolean = false): OAuthConsumer = {
    val consumerKey = requestMessage.getConsumerKey
    val callback    = requestMessage.getParameter(OAuth.OAUTH_CALLBACK)
    val scope = AuthorizationScope.fromString(requestMessage.getParameter(ScopeParameter))
    authentication.GetApplication(consumerKey)
      .map(a => {
      if(a.scope <=\= scope)
        throw new OAuthProblemException(ScopeFailed)

      new OAuthConsumer(if(checkCallback(callback)) callback else null, a.appId, a.appSecret, null)
    })
      .getOrElse(throw new OAuthProblemException(ConsumerFailed))


  }

  // OAuth Provider's methods
  def getConsumerById(appId: String): OAuthConsumer =
    authentication.GetApplication(appId)
      .map(a => {
        new OAuthConsumer(null, a.appId, a.appSecret, null)
      })
      .getOrElse(throw new OAuthProblemException(ConsumerFailed))

  /**
   * Get the request token and token secret for the given oauth_token.
   */
  def getAccessor(requestMessage: OAuthMessage): OAuthAccessor = {
    val consumerKey  = requestMessage.getConsumerKey
    val requestToken = requestMessage.getToken
    val verifier     = requestMessage.getParameter(OAuth.OAUTH_VERIFIER)
    authentication.GetRequestToken(consumerKey, requestToken)
      .map(t => {
        if(t.verifier.isEmpty || !t.verifier.get.equals(verifier))
          throw new OAuthProblemException(PermissionDenied)

        val accessor = new OAuthAccessor(getConsumer(requestMessage))
        accessor.requestToken = t.code
        accessor.tokenSecret = t.codeSecret
        accessor
      })
      .getOrElse(throw new OAuthProblemException(TokenExpired))
  }

  /**
   * Get the request token and token secret for the given oauth_token.
   */
  def getAuthorizeAccessor(requestMessage: OAuthMessage): OAuthAccessor = {
    val requestToken = requestMessage.getToken
    authentication.GetRequestToken(null, requestToken)
      .map(t => {
        val accessor = new OAuthAccessor(getConsumerById(t.applicationKey))
        accessor.requestToken = t.code
        accessor.tokenSecret = t.codeSecret
        accessor.setProperty(OAuthVerifier, t.verifier)
        checkAuthorize(accessor)
        accessor
      })
      .getOrElse(throw new OAuthProblemException(TokenExpired))
  }

  private def checkAuthorize(accessor: OAuthAccessor) = {
    accessor.setProperty(Authorized, true) // TODO check liferay authorization
  }

  /**
   * Generate a fresh request token and secret for a consumer.
   *
   */
  def generateRequestToken(accessor: OAuthAccessor): Unit = {
    val consumerKey = accessor.consumer.consumerKey
    val callback    = accessor.consumer.callbackURL
    val token       = UUID.randomUUID.toString
    val secret      = UUID.randomUUID.toString

    authentication.SetRequestToken(consumerKey, token, secret, callback)

    accessor.requestToken = token
    accessor.tokenSecret = secret
    accessor.accessToken = null
  }

  /**
   * Generate a fresh request token and secret for a consumer.
   *
   */
  def generateAccessToken(accessor: OAuthAccessor): Unit = {
    val consumerKey = accessor.consumer.consumerKey
    val requestCode = accessor.requestToken
    val token       = UUID.randomUUID.toString
    val tokenSecret = UUID.randomUUID.toString

    authentication.SetAccessToken(consumerKey, requestCode, token, tokenSecret)

    accessor.requestToken = null
    accessor.accessToken  = token
    accessor.tokenSecret  = tokenSecret
  }

  def handleException(e: Exception,
                      request: HttpServletRequest,
                      response: HttpServletResponse,
                      sendBody: Boolean = true) = {
    logger.info(e)

    var realm: String = if (request.isSecure) "https://" else "http://"
    realm += request.getLocalName
    OAuthServlet.handleException(response, e, realm, sendBody)
  }


  def markAsAuthorized(accessor: OAuthAccessor) = {
    val consumerKey = accessor.consumer.consumerKey
    val requestCode = accessor.requestToken
    val verifier    = UUID.randomUUID.hashCode.toString
    accessor.setProperty(OAuthVerifier, verifier)

    authentication.SetAuthorized(consumerKey, requestCode, verifier)
  }

  def checkCallback(callback: String) = callback != null && !callback.isEmpty
}
