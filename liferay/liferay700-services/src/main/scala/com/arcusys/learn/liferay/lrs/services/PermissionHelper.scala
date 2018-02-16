package com.arcusys.learn.liferay.lrs.services

import com.arcusys.learn.liferay.lrs.LiferayClasses.LUser
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal
import com.liferay.portal.kernel.security.permission.{PermissionChecker, PermissionCheckerFactoryUtil, PermissionThreadLocal}
import com.liferay.portal.kernel.service.UserLocalServiceUtil

object PermissionHelper {
  def create(user: LUser): PermissionChecker = PermissionCheckerFactoryUtil.create(user)

  def setPermissionChecker(permissionChecker: PermissionChecker): Unit =
    PermissionThreadLocal.setPermissionChecker(permissionChecker)


  def getPermissionChecker(): PermissionChecker = {
    PermissionThreadLocal.getPermissionChecker
  }

  def getPermissionChecker(user: LUser): PermissionChecker = {
    PermissionCheckerFactoryUtil.create(user)
  }

  def preparePermissionChecker(userId: Long): Unit = {
    val user = UserLocalServiceUtil.getUserById(userId)
    preparePermissionChecker(user)
  }

  def preparePermissionChecker(user: LUser): Unit = {
    val permissionChecker = PermissionCheckerFactoryUtil.create(user)

    PermissionThreadLocal.setPermissionChecker(permissionChecker)
    PrincipalThreadLocal.setName(user.getUserId)
  }
}
