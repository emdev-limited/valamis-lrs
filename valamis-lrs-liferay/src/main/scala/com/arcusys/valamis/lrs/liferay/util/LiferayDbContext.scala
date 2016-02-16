package com.arcusys.valamis.lrs.liferay.util

import com.liferay.portal.kernel.dao.shard.ShardUtil
import com.liferay.portal.model.Company
import com.liferay.portal.security.auth.CompanyThreadLocal
import com.liferay.portal.service.ShardLocalServiceUtil

class LiferayDbContext {

  def init() = {
    setScope(CompanyThreadLocal.getCompanyId)
  }

  def getScope: Long = {
    CompanyThreadLocal.getCompanyId
  }

  def setScope(companyId: Long): Unit = {
    if (ShardUtil.isEnabled) {
      CompanyThreadLocal.setCompanyId(companyId)

      val shard = ShardLocalServiceUtil.getShard(classOf[Company].getName, companyId)

      ShardUtil.setTargetSource(shard.getName)
    }
  }
}
