package com.arcusys.valamis.lrs.liferay.util

import com.arcusys.learn.liferay.services.{CompanyHelper, ShardUtilHelper}

class LiferayDbContext extends DbContext{

  def init() = {
    setScope(CompanyHelper.getCompanyId)
  }

  def getScope: Long = {
    CompanyHelper.getCompanyId
  }

  def setScope(companyId: Long): Unit = {
    ShardUtilHelper.initShardUtil(companyId)
  }
}
