package com.arcusys.valamis.lrs.liferay.servlet

import javax.servlet.http._
import com.arcusys.valamis.lrs.liferay._
import com.arcusys.valamis.lrs.services.LRS
import com.arcusys.valamis.lrs.tincan.Constants.Headers
import com.arcusys.valamis.lrs.tincan.TincanVersion
import com.google.inject.Injector
import com.liferay.portal.model.User
import com.liferay.portal.security.auth.{CompanyThreadLocal, PrincipalThreadLocal}
import com.liferay.portal.security.permission.{PermissionThreadLocal, PermissionCheckerFactoryUtil}
import com.liferay.portal.service.UserLocalServiceUtil
import com.liferay.portal.util.PortalUtil
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

abstract class BaseLrsServlet(inj: Injector) extends BaseServlet with JsonServlet with Loggable {

  protected lazy val lrs = inj.getInstance(classOf[LRS])

  override def setHeaders(response: HttpServletResponse): Unit = {
    response.addHeader(XExperienceAPIConsistentThrough, new DateTime().toString(ISODateTimeFormat.dateTime()))
    response.addHeader(Headers.Version, TincanVersion.ver101.toString)
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
}
