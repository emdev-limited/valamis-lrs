package com.arcusys.learn.liferay

import com.liferay.expando.kernel.exception.{DuplicateColumnNameException, DuplicateTableNameException}
import com.liferay.portal.kernel.bean.BeanLocator
import com.liferay.portal.kernel.dao.orm.DynamicQuery
import com.liferay.portal.kernel.exception._
import com.liferay.portal.kernel.model._
import com.liferay.portal.kernel.portlet.{LiferayPortletRequest, LiferayPortletResponse}
import com.liferay.portal.kernel.search._
import com.liferay.portal.kernel.security.permission.PermissionChecker
import com.liferay.portal.kernel.service.{BaseLocalService, ServiceContext}
import com.liferay.portal.kernel.struts.StrutsAction
import com.liferay.portal.kernel.theme.ThemeDisplay
import com.liferay.portal.kernel.upgrade.UpgradeProcess
import com.liferay.portal.kernel.util.UnicodeProperties

object LiferayClasses {
  type LBeanLocator = BeanLocator
  type LBaseLocalService = BaseLocalService
  type LBooleanQuery = BooleanQuery
  type LDocument = Document
  type LDocumentImpl = DocumentImpl
  type LDynamicQuery = DynamicQuery
  type LGroup = Group
  type LHits = Hits
  type LHitsOpenSearchImpl = HitsOpenSearchImpl
  type LLayout = Layout
  type LLayoutTypePortlet = LayoutTypePortlet
  type LLiferayPortletRequest = LiferayPortletRequest
  type LLiferayPortletResponse = LiferayPortletResponse
  type LPermissionChecker = PermissionChecker
  type LSearchContext = SearchContext
  type LServiceContext = ServiceContext
  type LStrutsAction = StrutsAction
  type LSummary = Summary
  type LThemeDisplay = ThemeDisplay
  type LUnicodeProperties = UnicodeProperties
  type LUpgradeProcess = UpgradeProcess
  type LUser = User
  type LOrganization = Organization
  type LAddress = Address
  type LCompany = Company




  // Exceptions
  type LNoSuchRoleException = NoSuchRoleException
  type LNoSuchGroupException = NoSuchGroupException
  type LNoSuchLayoutException = NoSuchLayoutException
  type LNoSuchUserException = NoSuchUserException
  type LNoSuchResourceActionException = NoSuchResourceActionException
  type LNoSuchCompanyException = NoSuchCompanyException

  type LDuplicateColumnNameException = DuplicateColumnNameException
  type LDuplicateTableNameException = DuplicateTableNameException
}
