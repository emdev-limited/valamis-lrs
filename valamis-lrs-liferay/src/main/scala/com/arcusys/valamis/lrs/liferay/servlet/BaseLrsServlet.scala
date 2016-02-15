package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.ServletConfig
import javax.servlet.http._

import com.arcusys.json.JsonHelper
import com.arcusys.valamis.lrs.{LrsType, Instrumented}
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.message.Broker
import com.arcusys.valamis.lrs.tincan.Constants.Headers
import com.arcusys.valamis.lrs.tincan.TincanVersion
import com.codahale.metrics.servlets.MetricsServlet
import com.codahale.metrics.{MetricRegistry, Timer}
import com.google.inject.name.Names
import com.google.inject.{Key, Injector}
import com.liferay.portal.model.User
import com.liferay.portal.security.auth.{CompanyThreadLocal, PrincipalThreadLocal}
import com.liferay.portal.security.permission.{PermissionCheckerFactoryUtil, PermissionThreadLocal}
import com.liferay.portal.service.UserLocalServiceUtil
import com.liferay.portal.util.PortalUtil
import com.arcusys.valamis.lrs.protocol._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

abstract class BaseLrsServlet(inj: Injector) extends BaseServlet with JsonServlet with LrsTypeLocator  with Instrumented {

  protected lazy val lrsType  = LrsType.Simple
  protected lazy val broker   = inj.getInstance(classOf[Broker])
  /// TODO: Should be Lrs type
  protected lazy val lrs = inj getInstance Key.get(
    classOf[JdbcLrs],
    Names.named(lrsType.toString)
  )

  protected lazy val reporter = inj getInstance Key.get(
    classOf[ValamisReporter],
    Names.named(lrsType.toString)
  )

  protected lazy val securityManager = inj getInstance Key.get(
    classOf[SecurityManager],
    Names.named(lrsType.toString)
  )

  protected lazy val executionContext = inj getInstance Key.get(
    classOf[ExecutionContext],
    Names.named(lrsType.toString)
  )


  private val MessageHead = "Valamis LRS"
  val ServletName: String

  protected var requestsTimer: Timer = _
  protected var registry: MetricRegistry = _

  override def setHeaders(response: HttpServletResponse): Unit = {
    response.addHeader(XExperienceAPIConsistentThrough, new DateTime().toString(ISODateTimeFormat.dateTime()))
    response.addHeader(Headers.Version, TincanVersion.ver101.toString)
  }

  override def init(config: ServletConfig): Unit = {
    getRegistry(config)
    requestsTimer = registry timer ServletName
  }

  def getRegistry(config: ServletConfig) {
    val context = config.getServletContext()
    this.registry = context
      .getAttribute(MetricsServlet.METRICS_REGISTRY)
      .asInstanceOf[MetricRegistry]
  }

  protected def noContent = throw new NoSuchElementException

  def getUserByRequest(request: HttpServletRequest): User = {

    val user = PortalUtil.getUser(request) match {
      case u: User if u == null  =>
        UserLocalServiceUtil.getDefaultUser(PortalUtil.getDefaultCompanyId)

      case u: User => u
    }

    val permissionChecker = PermissionCheckerFactoryUtil.create(user)

    PermissionThreadLocal.setPermissionChecker(permissionChecker)
    PrincipalThreadLocal.setName(user.getUserId)
    CompanyThreadLocal.setCompanyId(user.getCompanyId)
    user
  }

  implicit class MessageExtension[T <: Message] (val msg: T)(implicit manifest: Manifest[T]) {
    def sendMessage() = {
      val messageContent = JsonHelper.toJson(msg)
      broker !(MessageHead, messageContent)
    }
  }
}
