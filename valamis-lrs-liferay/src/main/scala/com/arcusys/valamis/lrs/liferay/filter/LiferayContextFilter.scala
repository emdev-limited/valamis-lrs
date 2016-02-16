package com.arcusys.valamis.lrs.liferay.filter

import javax.inject.Named
import javax.servlet._
import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.liferay.util.LiferayDbContext
import com.google.inject.{Inject, Singleton}
import com.liferay.portal.util.PortalUtil

@Singleton
class LiferayContextFilter @Inject()(@Named("Simple") liferayContext: LiferayDbContext)
  extends Filter {

  override def doFilter(request: ServletRequest,
                        response: ServletResponse,
                        filterChain: FilterChain) = {
    val companyId = PortalUtil.getCompanyId(request.asInstanceOf[HttpServletRequest])

    liferayContext.setScope(companyId)

    filterChain.doFilter(request, response)
  }

  override def init(filterConfig: FilterConfig) = Unit

  override def destroy() = Unit
}
