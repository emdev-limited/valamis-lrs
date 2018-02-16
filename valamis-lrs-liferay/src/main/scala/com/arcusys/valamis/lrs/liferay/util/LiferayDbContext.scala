package com.arcusys.valamis.lrs.liferay.util

import com.arcusys.learn.liferay.lrs.services.{CompanyHelper, ShardUtilHelper}

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

class DummyDbContext extends DbContext {
  override def init(): Unit = ()

  override def setScope(companyId: Long): Unit = ()

  override def getScope: Long = 0L
}
