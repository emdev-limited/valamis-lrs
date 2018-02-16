package com.arcusys.learn.liferay.lrs.services

import com.liferay.portal.service.ClassNameLocalServiceUtil

object ClassNameLocalServiceHelper {
  def getClassNameId(value: String): Long = ClassNameLocalServiceUtil.getClassNameId(value)
}
