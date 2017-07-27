package com.arcusys.learn.liferay.lrs.services

import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal

object PrincipalHelper {
  def setName(userId: Long) = PrincipalThreadLocal.setName(userId)

}
