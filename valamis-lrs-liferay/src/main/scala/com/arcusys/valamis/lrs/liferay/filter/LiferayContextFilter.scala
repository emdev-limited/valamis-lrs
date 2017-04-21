package com.arcusys.valamis.lrs.liferay.filter

import javax.servlet._
import javax.servlet.http.HttpServletRequest

import com.arcusys.valamis.lrs.liferay.util.DbContext
import com.google.inject.{Inject, Singleton}
import com.arcusys.learn.liferay.lrs.util.PortalUtilHelper

@Singleton
class LiferayContextFilter @Inject()(liferayContext: DbContext)
  extends Filter {

  override def doFilter(request: ServletRequest,
                        response: ServletResponse,
                        filterChain: FilterChain) = {
    val companyId = PortalUtilHelper.getCompanyId(request.asInstanceOf[HttpServletRequest])

    liferayContext.setScope(companyId)

    filterChain.doFilter(request, response)
  }

  override def init(filterConfig: FilterConfig) = Unit

  override def destroy() = Unit
}
