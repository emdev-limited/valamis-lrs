package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.ServletConfig
import javax.servlet.http._

import com.arcusys.json.JsonHelper
import com.arcusys.learn.liferay.lrs.LiferayClasses.LUser
import com.arcusys.learn.liferay.lrs.services.{CompanyHelper, PermissionHelper, PrincipalHelper, UserLocalServiceHelper}
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.liferay.util._
import com.arcusys.valamis.lrs.liferay.message.Broker
import com.arcusys.valamis.lrs.protocol._
import com.arcusys.valamis.lrs.tincan.Constants.Headers
import com.arcusys.valamis.lrs.tincan.TincanVersion
import com.arcusys.valamis.lrs._
import com.codahale.metrics.servlets.MetricsServlet
import com.codahale.metrics.{MetricRegistry, Timer}
import com.google.inject.{Injector, Key}
import com.arcusys.learn.liferay.lrs.util.PortalUtilHelper
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

abstract class BaseLrsServlet(inj: Injector) extends BaseServlet with Loggable with JsonServlet with Instrumented {

  protected lazy val broker   = inj.getInstance(classOf[Broker])
  protected lazy val lrs = inj getInstance Key.get(classOf[Lrs])
  protected lazy val sparkProcessor = inj getInstance Key.get(classOf[SparkProcessor])

  protected lazy val reporter = inj getInstance Key.get(classOf[ValamisReporter])

  protected lazy val securityManager = inj getInstance Key.get(classOf[SecurityManager])


  private val MessageHead = "Valamis LRS"
  val ServletName: String

  protected var requestsTimer: Option[Timer] = None

  override def setHeaders(response: HttpServletResponse): Unit = {
    response.addHeader(XExperienceAPIConsistentThrough, new DateTime().toString(ISODateTimeFormat.dateTime()))
    response.addHeader(Headers.Version, TincanVersion.ver101.toString)
  }

  override def init(config: ServletConfig): Unit = {
    initMetrics(config)
  }

  def initMetrics(config: ServletConfig) {
    val context = Option(config.getServletContext)
    val registry = context.flatMap { ctx =>
      Option(ctx.getAttribute(MetricsServlet.METRICS_REGISTRY)).map(_.asInstanceOf[MetricRegistry])
    }
    requestsTimer = registry.map(_.timer(ServletName))
  }

  protected def noContent = throw new NoSuchElementException

  def getUserByRequest(request: HttpServletRequest): LUser = {

    val user = PortalUtilHelper.getUser(request) match {
      case u: LUser if u == null  =>
        UserLocalServiceHelper().getDefaultUser(PortalUtilHelper.getDefaultCompanyId)

      case u: LUser => u
    }

    val permissionChecker = PermissionHelper.create(user)

    PermissionHelper.setPermissionChecker(permissionChecker)
    PrincipalHelper.setName(user.getUserId)
    CompanyHelper.setCompanyId(user.getCompanyId)
    user
  }

  implicit class MessageExtension[T <: Message] (val msg: T)(implicit manifest: Manifest[T]) {
    def sendMessage() = {
      val messageContent = JsonHelper.toJson(msg)
      broker !(MessageHead, messageContent)
    }
  }
}
