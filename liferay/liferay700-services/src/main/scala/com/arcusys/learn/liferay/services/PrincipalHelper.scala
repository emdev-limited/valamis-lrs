package com.arcusys.learn.liferay.services

import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal

object PrincipalHelper {
  def setName(userId: Long) = PrincipalThreadLocal.setName(userId)

}
