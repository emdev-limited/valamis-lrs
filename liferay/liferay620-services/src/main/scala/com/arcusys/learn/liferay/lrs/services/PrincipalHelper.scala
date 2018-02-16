package com.arcusys.learn.liferay.lrs.services

import com.liferay.portal.security.auth.PrincipalThreadLocal


object PrincipalHelper {
  def setName(userId: Long) = PrincipalThreadLocal.setName(userId)

}
