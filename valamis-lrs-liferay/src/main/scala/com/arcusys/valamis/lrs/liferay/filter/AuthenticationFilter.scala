package com.arcusys.valamis.lrs.liferay.filter

import java.util.regex.Pattern
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.xml.bind.DatatypeConverter

import com.arcusys.valamis.lrs.LrsType
import com.arcusys.valamis.lrs.jdbc.SecurityManager
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.exception._
import com.arcusys.valamis.lrs.security.AuthenticationStatus
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.google.inject._
import com.google.inject.name.Names
import com.liferay.portal.util.PortalUtil
import net.oauth._
import net.oauth.server.OAuthServlet

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

@Singleton
class AuthenticationFilter @Inject()(injector: Injector) extends Filter with LrsTypeLocator {

  private lazy val lrsType = LrsType.Simple
  private lazy val securityManager = injector.getInstance(
    Key.get(classOf[SecurityManager], Names.named(lrsType.toString))
  )

  private val basicDelim = Pattern.compile(":")

  override def doFilter(request:     ServletRequest,
                        response:    ServletResponse,
                        filterChain: FilterChain) {

      val req = request.asInstanceOf[HttpServletRequest]
      val res = response.asInstanceOf[HttpServletResponse]

      if (req.getMethod.equalsIgnoreCase(Options)) {
        filterChain.doFilter(request, response)
        return
      }

      val headerNames = req.getHeaderNames.asScala.map(x => x.toLowerCase)
      if (!headerNames.contains(Authorization.toLowerCase)) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
        return
      }

      Try {

        val authString = req.getHeader(Authorization)
        val scope = getScope(req)

        if (authString != null && authString.startsWith(Basic)) {
          val auth = authString.substring(Basic.length)
          val loginPassPair = new String(DatatypeConverter.parseBase64Binary(auth))
          val basic = basicDelim.split(loginPassPair)

          if (basic.length == 2) checkByBasic(basic(0), basic(1), scope)
          else throw new InvalidOrMissingArgumentException("Basic authority invalid")

        } else checkByToken(req, scope)

      } match {
        case Failure(_) => res.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        case Success(v) => v match {
          case AuthenticationStatus.Allowed => filterChain.doFilter(request, response)
          case AuthenticationStatus.Denied  => res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
        }
      }
  }

  override def init(filterConfig: FilterConfig) = {}

  override def destroy() = {}

  private def checkByBasic(login: String, pswd: String, scope: AuthorizationScope.ValueSet) =
    securityManager.checkByBasic(login, pswd, scope) match {
      case AuthenticationStatus.Forbidden => throw new ForbiddenException
      case e => e
    }

  private def checkByToken(req: HttpServletRequest, scope: AuthorizationScope.ValueSet) = {
    val validator      = new SimpleOAuthValidator
    val requestMessage = OAuthServlet.getMessage(req, PortalUtil.getCurrentCompleteURL(req))
    val accessor       = getAccessor(requestMessage)

    validator.validateMessage(requestMessage, accessor)
    if (accessor.accessToken == null || accessor.accessToken.isEmpty)
      AuthenticationStatus.Allowed
    else
      securityManager.checkByToken(accessor.consumer.consumerKey, accessor.accessToken, scope) match {
        case AuthenticationStatus.Forbidden => throw new ForbiddenException
        case e => e
      }
  }

  /**
   * Get the consumer
   */
  private def getConsumer(requestMessage: OAuthMessage): OAuthConsumer = {
    val consumerKey = requestMessage.getConsumerKey
    val app = securityManager.getApplication(consumerKey)
    app.map(a => {
      new OAuthConsumer(null, a.appId, a.appSecret, null)
    })
      .getOrElse(throw new OAuthProblemException("consumer_failed"))
  }

  /**
   * Get the access token and token secret for the given oauth_token.
   */
  private def getAccessor(requestMessage: OAuthMessage): OAuthAccessor = {
    val consumerKey = requestMessage.getConsumerKey
    val accessToken = requestMessage.getToken
    if (accessToken != null && !accessToken.isEmpty) {
      val verifier = requestMessage.getParameter(OAuth.OAUTH_VERIFIER)
      val token =  securityManager.getAccessToken(consumerKey, accessToken)

      token.map(t => {
        if (!t.verifier.isDefined || !t.verifier.get.equals(verifier))
          throw new OAuthProblemException("permission_denied")
        val accessor = new OAuthAccessor(getConsumer(requestMessage))
        accessor.accessToken = t.token.getOrElse(throw new OAuthProblemException("access_token_failed"))
        accessor.tokenSecret = t.tokenSecret.getOrElse(throw new OAuthProblemException("access_token_secret_failed"))
        accessor
      })
        .getOrElse(throw new OAuthProblemException("token_expired"))

    }
    else new OAuthAccessor(getConsumer(requestMessage))
  }

  private def getScope(req: HttpServletRequest) = req.getServletPath match {
    case path if path.contains("activities/profile") &&
      req.getMethod.equalsIgnoreCase(Get) => AuthorizationScope.ProfileRead.toValueSet

    case path if path.contains("activities/state") &&
      req.getMethod.equalsIgnoreCase(Get) => AuthorizationScope.StateRead.toValueSet

    case path if path.contains("activity/state") &&
      req.getMethod.equalsIgnoreCase(Get) => AuthorizationScope.StateRead.toValueSet

    case path if path.contains("statements") &&
      req.getMethod.equalsIgnoreCase(Get) => AuthorizationScope.StatementsRead

    case path if path.contains("activities/profile") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.ProfileWrite.toValueSet

    case path if path.contains("activities/state") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.StateWrite.toValueSet

    case path if path.contains("activity/state") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.StateWrite.toValueSet

    case path if path.contains("statements") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.StatementsWrite.toValueSet

    case path if path.contains("activities") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.Define.toValueSet

    case path if path.contains("agents") &&
      (req.getMethod.equalsIgnoreCase(Post) | req.getMethod.equalsIgnoreCase(Put)) => AuthorizationScope.Define.toValueSet

    case path if req.getMethod.equalsIgnoreCase(Get) => AuthorizationScope.AllRead
    case _ => AuthorizationScope.All
  }
}
