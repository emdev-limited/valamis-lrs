package com.arcusys.learn.liferay.lrs.services

import com.liferay.portal.security.auth.CompanyThreadLocal

object CompanyHelper {
  def setCompanyId(companyId: Long) = CompanyThreadLocal.setCompanyId(companyId)

  def getCompanyId: Long = CompanyThreadLocal.getCompanyId

}
