package com.arcusys.valamis.lrs.liferay.util

trait DbContext {

  def init()

  def getScope: Long

  def setScope(companyId: Long): Unit
}
