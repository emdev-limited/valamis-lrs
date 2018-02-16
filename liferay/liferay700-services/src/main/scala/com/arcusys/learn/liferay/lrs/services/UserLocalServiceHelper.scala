package com.arcusys.learn.liferay.lrs.services

import java.util
import java.util.Locale

import com.arcusys.learn.liferay.lrs.LiferayClasses.LUser
import com.arcusys.learn.liferay.lrs.services.utils.dynamicQuery._
import com.liferay.portal.kernel.dao.orm._
import com.liferay.portal.kernel.model.User
import com.liferay.portal.kernel.service.{ServiceContext, UserLocalServiceUtil}
import com.liferay.portal.kernel.util.{DigesterUtil, HttpUtil, OrderByComparator}
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil

import scala.collection.JavaConverters._

object UserLocalServiceHelper {


  def apply() = new UserLocalServiceHelper {}
}

trait UserLocalServiceHelper {
  val IdKey = "userId"
  val FirstNameKey = "firstName"
  val LastNameKey = "lastName"

  def fetchUserByUuidAndCompanyId(name: String, companyId: Long): LUser = UserLocalServiceUtil.fetchUserByUuidAndCompanyId(name, companyId)

  def fetchUserByEmailAddress(companyId: Long, email: String): LUser = UserLocalServiceUtil.fetchUserByEmailAddress(companyId, email)

  def fetchUser(userId: Long): LUser = UserLocalServiceUtil.fetchUser(userId)

  def getUserByEmailAddress(companyId: Long, userEmail: String): LUser = UserLocalServiceUtil.getUserByEmailAddress(companyId, userEmail)

  def getUserById(userId: Long): LUser = UserLocalServiceUtil.getUserById(userId)

  def dynamicQuery: DynamicQuery = {
    UserLocalServiceUtil.dynamicQuery()
  }

  def dynamicQuery(dynamicQuery: DynamicQuery): util.List[LUser] = {
    UserLocalServiceUtil.dynamicQuery[User](dynamicQuery)
  }

  def dynamicQuery(dynamicQuery: DynamicQuery, start: Int, end: Int): util.List[LUser] = {
    UserLocalServiceUtil.dynamicQuery[User](dynamicQuery, start, end)
  }

  def dynamicQuery(dynamicQuery: DynamicQuery, start: Int, end: Int, order: OrderByComparator[User]): util.List[LUser] = {
    UserLocalServiceUtil.dynamicQuery[User](dynamicQuery, start, end, order)
  }

  def dynamicQueryCount(dynamicQuery: DynamicQuery): Long = {
    UserLocalServiceUtil.dynamicQueryCount(dynamicQuery)
  }

  def getCount(userIds: Seq[Long],
               contains: Boolean,
               companyId: Long,
               nameLike: Option[String],
               orgId: Option[Long] = None): Long = {
    val query = dynamicQuery(userIds, contains, companyId, nameLike, orgId)
    query.map(dynamicQueryCount).getOrElse(0L)
  }

  def getUsers(userIds: Seq[Long],
               contains: Boolean,
               companyId: Long,
               nameLike: Option[String],
               ascending: Boolean,
               startEnd: Option[(Int, Int)],
               orgId: Option[Long] = None): Seq[LUser] = {
    val query = UserLocalServiceHelper().dynamicQuery(userIds, contains, companyId, nameLike, orgId)

    query.map(q => dynamicQuery(q, ascending, startEnd)).getOrElse(Nil)
  }

  def dynamicQuery(query: DynamicQuery,
                   ascending: Boolean,
                   startEnd: Option[(Int, Int)]): Seq[LUser] = {
    val (start, end) = startEnd.getOrElse((-1, -1))

    val q = if (ascending) {
      query.addOrder(OrderFactoryUtil.asc(FirstNameKey)).addOrder(OrderFactoryUtil.asc(LastNameKey))
    } else {
      query.addOrder(OrderFactoryUtil.desc(FirstNameKey)).addOrder(OrderFactoryUtil.desc(LastNameKey))
    }

    dynamicQuery(q, start, end)
      .asScala
  }

  def dynamicQuery(userIds: Seq[Long],
                   contains: Boolean,
                   companyId: Long,
                   nameLike: Option[String],
                   orgId: Option[Long]): Option[DynamicQuery] = {
    var query = UserLocalServiceHelper().dynamicQuery
      .add(RestrictionsFactoryUtil.eq("defaultUser", false))
      .add(RestrictionsFactoryUtil.eq("companyId", companyId))
      .addInSetRestriction(IdKey, userIds, contains)

    if (nameLike.isDefined) {
      query = query.add(RestrictionsFactoryUtil.or(
        RestrictionsFactoryUtil.ilike(FirstNameKey, nameLike.get),
        RestrictionsFactoryUtil.ilike(LastNameKey, nameLike.get)
      ))
    }

    if (orgId.isDefined) {

      val userIdsByOrg = orgId.map(getOrganizationUserIds)
        .getOrElse(Seq())
        .distinct

      userIdsByOrg match {
        case Seq() => None
        case ids: Seq[Long] => Some(query.add(RestrictionsFactoryUtil.in("userId", userIdsByOrg.asJava)))
      }
    }
    else {
      Some(query)
    }
  }

  def getCompanyUsers(companyId: Long, start: Int, end: Int): java.util.List[User] =
    UserLocalServiceUtil.getCompanyUsers(companyId, start, end)

  def getOrganizationUsers(organizationId: Long): java.util.List[User] =
    UserLocalServiceUtil.getOrganizationUsers(organizationId)

  def getUsers(start: Int, end: Int): java.util.List[User] = UserLocalServiceUtil.getUsers(start, end)

  def getUsers(userIds: Seq[Long]): Seq[User] = {
    if (userIds.isEmpty) {
      Nil
    } else {
      val query = UserLocalServiceHelper().dynamicQuery
        .addInSetRestriction(IdKey, userIds, contains = true)

      UserLocalServiceHelper().dynamicQuery(query).asScala
    }
  }

  def getUser(userId: Long): User = UserLocalServiceUtil.getUser(userId)

  def getUserById(companyId: Long, userId: Long): User = UserLocalServiceUtil.getUserById(companyId, userId)

  def getRoleUsersCount(roleId: Long): Int = UserLocalServiceUtil.getRoleUsersCount(roleId)

  def getUsersByRoleId(liferayRoleId: Long): java.util.List[User] = UserLocalServiceUtil.getRoleUsers(liferayRoleId)

  def addGroupUsers(groupId: Long, userIds: Array[Long]) {
    UserLocalServiceUtil.addGroupUsers(groupId, userIds)
  }

  def getGroupUsers(groupId: Long): java.util.List[User] =
    UserLocalServiceUtil.getGroupUsers(groupId)

  def getGroupUserIds(groupId: Long): Seq[Long] =
    UserLocalServiceUtil.getGroupUserIds(groupId)

  def getOrganizationUserIds(orgId: Long): Seq[Long] =
    UserLocalServiceUtil.getOrganizationUserIds(orgId)

  def getDefaultUserId(companyId: Long): Long = UserLocalServiceUtil.getDefaultUserId(companyId)

  def getDefaultUser(companyId: Long): LUser = UserLocalServiceUtil.getDefaultUser(companyId)

  def unsetOrganizationUsers(organizationId: Long, userIds: Array[Long]) {
    UserLocalServiceUtil.unsetOrganizationUsers(organizationId, userIds)
  }

  def addUser(creatorUserId: Long, companyId: Long, autoPassword: Boolean,
    password1: String, password2: String,
    autoScreenName: Boolean, screenName: String, emailAddress: String,
    facebookId: Long, openId: String, locale: Locale,
    firstName: String, middleName: String, lastName: String,
    prefixId: Int, suffixId: Int, male: Boolean,
    birthdayMonth: Int, birthdayDay: Int, birthdayYear: Int,
    jobTitle: String, groupIds: Array[Long], organizationIds: Array[Long],
    roleIds: Array[Long], userGroupIds: Array[Long], sendEmail: Boolean,
    serviceContext: ServiceContext): User =
    UserLocalServiceUtil.addUser(creatorUserId, companyId, autoPassword, password1, password2,
      autoScreenName, screenName, emailAddress, facebookId, openId, locale,
      firstName, middleName, lastName, prefixId, suffixId, male,
      birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds,
      roleIds, userGroupIds, sendEmail, serviceContext)

  def updatePortrait(userId: Long, bytes: Array[Byte]): User = UserLocalServiceUtil.updatePortrait(userId, bytes)

  def updateReminderQuery(userId: Long, question: String, answer: String): User =
    UserLocalServiceUtil.updateReminderQuery(userId, question, answer)

  def getPortraitTime(portraitId: Long) = {
    WebServerServletTokenUtil.getToken(portraitId)
  }
  def getPortraitToken(user: User) = {
    HttpUtil.encodeURL(DigesterUtil.digest(user.getUserUuid))
  }

}
