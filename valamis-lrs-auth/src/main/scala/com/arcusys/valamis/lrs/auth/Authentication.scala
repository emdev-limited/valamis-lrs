package com.arcusys.valamis.lrs.auth

import java.util.UUID
import javax.inject.Inject
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.auth.datasource.{DataQuery, DataContext}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 22.06.15.
 */
class Authentication @Inject()(dr:  JdbcDriver,
                               db: JdbcBackend#Database)
  extends DataContext(dr, db)
  with DataQuery {

  import driver.simple._
  import jodaSupport._

  val expiredPeriod = 60 * 60 * 24
  val log = LoggerFactory.getLogger("com.arcusys")

  // One day
  def expiredDateTime = DateTime.now.minusSeconds(expiredPeriod)


  def GetCallback(token: String) = database.withSession { implicit session =>
    tokens
      .filter { x => x.code === token }
      .map { x => x.callback }
      .firstOption
  }

  def GetApplications(take: Int, offset: Int) = database.withSession { implicit session =>
    applications
      .take(take)
      .list
      .drop(offset)
  }

  def CheckByToken(appId: String,
                   token: String,
                   scopeRequest: AuthorizationScope.ValueSet) =
   database.withSession { implicit s =>

    getAppScope(appId) match {
      case None        => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val isExistValidToken = tokens
          .filter(t => t.appId === appId)
          .filter(t => t.token === token)
          .filter(t => t.issueAt > expiredDateTime)
          .firstOption.isDefined

        AuthenticationStatus(isExistValidToken && accessRightSuccess)
    }
  }

  def CheckByBasic(clientId: String,
                   secret: String,
                   scopeRequest: AuthorizationScope.ValueSet) =
  database.withSession { implicit s =>

    getAppScope(clientId) match {
      case None        => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val valid = applications
          .filter(x => x.appId === clientId)
          .filter(x => x.appSecret === secret)
          .firstOption.isDefined

        AuthenticationStatus(valid && accessRightSuccess)
    }
  }

  def SetAuthorized(appId: String,
                    requestCode: String,
                    verified: String) =
  database.withSession { implicit s =>

    tokens
      .filter(t => t.appId === appId)
      .filter(t => t.code === requestCode)
      .map(t => t.verifier)
      .update(verified) match {
      case e if e >= 0 =>
        log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode")

      case e if e < 0  =>
        log.error(s"Set access token failure: ${tokens.updateStatement} with" +
          s" User Id = None, App Id = $appId, Request Code = $requestCode")
    }
  }

  def SetAccessToken(appId: String,
                     requestCode: String,
                     accessCode: String,
                     accessSecret: String) =
  database.withSession { implicit session =>

    tokens
      .filter(t => t.appId === appId && t.code === requestCode)
      .map(t => (t.issueAt, t.token, t.tokenSecret))
      .update((DateTime.now, accessCode, accessSecret)) match {
      case e if e >= 0 =>
        log.debug(s"Set access token success: User Id = None, App Id = $appId, Access Code = $accessCode")

      case e if e < 0  =>
        log.error(s"Set access token failure: ${tokens.updateStatement} with User Id = None, App Id = $appId, Access Code = $accessCode")
    }
  }

  def SetRequestToken(appId: String,
                      requestCode: String,
                      requestSecret: String,
                      callback: String) =
  database.withSession { implicit session =>

    val token = Token(None, appId, requestCode, requestSecret, callback, DateTime.now())
    tokens.insert(token) match {
      case e if e >= 0 =>
        log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode, callback = $callback")

      case e if e < 0  =>
        log.error(s"Set access token failure: ${tokens.insertStatement} with User Id = None, App Id = $appId, Access Code = $requestCode")
    }
  }

  def GetRequestToken(appId: String,
                      requestCode: String) =
  database.withSession { implicit session =>

    val token = tokens
      .filter(t => t.code === requestCode)
      .filter(t => t.token.isNull)
      .filter(t => t.issueAt > expiredDateTime)
      .firstOption

    token
  }

  def GetAccessToken(appId: String,
                     token: String) =
  database.withSession { implicit session =>

    val foundToken = tokens
      .filter(t => t.appId === appId)
      .filter(t => t.token === token)
      .filter(t => t.issueAt > expiredDateTime)
      .firstOption

    foundToken
  }

  def RegistrationApp(appName: String,
                      appDescription: Option[String],
                      scope: AuthorizationScope.ValueSet,
                      authType: AuthenticationType.Type) =
  database.withSession { implicit session =>
    val app = Application(
      appId = UUID.randomUUID.toString,
      name = appName,
      description = appDescription.orNull,
      appSecret = UUID.randomUUID.toString,
      scope = scope,
      regDateTime = DateTime.now,
      authType = authType
    )
    applications.insert(app) match {
      case e if e >= 0 =>
        log.debug(s"Registered application success: App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
        app.toOption

      case e if e < 0  =>
        log.error(s"Registered application failure: ${applications.insertStatement} with " +
          s"App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
        None
    }
  }

  def GetApplication(appId: String) =
  database.withSession { implicit session =>
    getApp(appId)
  }

  def UpdateApplication(appId: String,
                        name: String,
                        desc: Option[String],
                        scope: AuthorizationScope.ValueSet,
                        authType: AuthenticationType.Type) =
  database.withSession { implicit session =>
    val query = applications
      .filter(x => x.appId === appId)
      .map(x => (x.name, x.scope, x.description, x.authType))

    query.update((name, scope, desc.orNull, authType)) match {
      case e if e >= 0 =>
        log.debug(s"Update application success: App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")

      case e if e < 0  =>
        log.error(s"Update application failure: ${query.updateStatement} with " +
          s"App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")
    }
  }

  def DeleteApplication(appId: String) =
  database.withSession { implicit session =>
    val query = applications.filter(x => x.appId === appId)

    query.delete match {
      case e if e >= 0 =>
        log.debug(s"Delete application success: App Id = ${appId}")

      case e if e < 0  =>
        log.error(s"Delete application failure: ${query.deleteStatement} with App Id = ${appId}")
    }
  }

  def BlockApplication(appId: String) =
  database.withSession { implicit session =>
    val query = applications
      .filter { app => app.appId === appId }
      .map { app => app.isActive }

    query.update(false) match {
      case e if e >= 0  =>
        log.debug(s"Block application success: App Id = ${appId}")

      case e if e < 0  =>
        log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
    }
  }

  def UnblockApplication(appId: String) =
  database.withSession { implicit session =>
    val query = applications
      .filter { app => app.appId === appId }
      .map { app => app.isActive }

    query.update(true) match {
      case e if e >= 0 =>
        log.debug(s"Block application success: App Id = ${appId}")

      case e if e < 0  =>
        log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
    }
  }

  def Clear() =  database.withSession { implicit session =>

    val query = tokens
      .filter { t => t.issueAt < expiredDateTime }

    query.delete match {
      case e if e >= 0 =>
        log.debug(s"Expired tokens remove success: ${e} tokens was removed")

      case e if e < 0 =>
        log.error(s"Expired tokens remove failure: ${query.deleteStatement}")
    }
  }
}
