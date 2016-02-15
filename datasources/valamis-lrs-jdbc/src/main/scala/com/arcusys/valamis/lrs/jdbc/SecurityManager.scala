package com.arcusys.valamis.lrs.jdbc

import java.util.UUID

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.SecurityDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.{ApplicationRow, TokenRow}
import com.arcusys.valamis.lrs.security.{AuthenticationStatus, AuthenticationType}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait SecurityManager extends SecurityDataContext {

  val executionContext: ExecutionContext

  import executionContext.driver.simple._
  import jodaSupport._

  val expiredPeriod = 60 * 60 * 24
  val log = LoggerFactory.getLogger("com.arcusys")

  // One day
  def expiredDateTime = DateTime.now.minusSeconds(expiredPeriod)

  private def getAppScope(appId: String) = {
    val query = applications filter {
      x => x.appId === appId
    } map {
      app => app.scope
    }

    executionContext from query selectFirstOpt
  }

  def getCallback(token: String) = {
    val query = tokens filter { x => x.code === token } map { x => x.callback }

    executionContext from query selectFirstOpt
  }

  def getApplications(count: Int, offset: Int) =  {
    val query = applications take count drop offset

    executionContext from query select
  }

  def checkByToken(appId: String,
                   token: String,
                   scopeRequest: AuthorizationScope.ValueSet) =
    getAppScope(appId) match {
      case None        => AuthenticationStatus.Denied
      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val query = tokens filter { t =>
          (t.appId === appId) &&
          (t.token === token) &&
          (t.issueAt > expiredDateTime)
        }

        val isExistValidToken = executionContext from query exists

        if (accessRightSuccess) AuthenticationStatus(isExistValidToken)
        else AuthenticationStatus.Forbidden
    }

  def checkByBasic(clientId:     String,
                   secret:       String,
                   scopeRequest: AuthorizationScope.ValueSet) =
    getAppScope(clientId) match {
      case None        => AuthenticationStatus.Denied

      case Some(scope) =>
        val accessRightSuccess = scope <== scopeRequest

        val query = applications filter { x =>
          (x.appId === clientId) && (x.appSecret === secret)
        }

        val valid = executionContext from query exists

        if (accessRightSuccess) AuthenticationStatus(valid)
        else AuthenticationStatus.Forbidden
    }

  def setAuthorized(appId:       String,
                    requestCode: String,
                    verified:    String) = {
      val query = tokens filter { t =>
        (t.appId === appId) && (t.code === requestCode)

      } map { t => t.verifier }

      executionContext updateTo query value verified.toOption match {
        case e if e >= 0 =>
          log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode")

        case e if e < 0  =>
          log.error(s"Set access token failure: ${query.updateStatement} with User Id = None, App Id = $appId, Request Code = $requestCode")
      }
    }

  def setAccessToken(appId:        String,
                     requestCode:  String,
                     accessCode:   String,
                     accessSecret: String) = {

      val query = tokens filter {
        t => (t.appId === appId) && (t.code === requestCode)
      } map { t => (t.issueAt, t.token, t.tokenSecret) }

      executionContext updateTo query value (DateTime.now, accessCode.toOption, accessSecret.toOption) match {
        case e if e >= 0 =>
          log.debug(s"Set access token success: User Id = None, App Id = $appId, Access Code = $accessCode")

        case e if e < 0  =>
          log.error(s"Set access token failure: ${tokens.updateStatement} with User Id = None, App Id = $appId, Access Code = $accessCode")
      }
    }

  def setRequestToken(appId:         String,
                      requestCode:   String,
                      requestSecret: String,
                      callback:      String) = {
    val token = TokenRow(None, appId, requestCode, requestSecret, callback, DateTime.now)

    executionContext insertTo tokens value token match {
      case e if e >= 0 =>
        log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode, callback = $callback")

      case e if e < 0 =>
        log.error(s"Set access token failure: ${tokens.insertStatement} with User Id = None, App Id = $appId, Access Code = $requestCode")
    }
  }

  def getRequestToken(appId: String,
                      requestCode: String) = {
    val query = tokens
      .filter(t => t.code === requestCode)
      .filter(t => t.token.isNull)
      .filter(t => t.issueAt > expiredDateTime)

    executionContext from query selectFirstOpt
  }

  def getAccessToken(appId: String,
                     token: String) = {
    val query = tokens
      .filter(t => t.appId === appId)
      .filter(t => t.token === token)
      .filter(t => t.issueAt > expiredDateTime)

    executionContext from query selectFirstOpt
  }

  def registrationApp(appName:        String,
                      appDescription: Option[String],
                      scope:          AuthorizationScope.ValueSet,
                      authType:       AuthenticationType.Type) = {
    val app = ApplicationRow(
      appId = UUID.randomUUID.toString,
      name = appName,
      description = appDescription.orNull,
      appSecret = UUID.randomUUID.toString,
      scope = scope,
      regDateTime = DateTime.now,
      authType = authType
    )

    executionContext insertTo applications value app match {
      case e if e >= 0 =>
        log.debug(s"Registered application success: App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
        app ?

      case e if e < 0 =>
        log.error(s"Registered application failure: ${applications.insertStatement} with App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
        None
    }
  }

  def getApplication(appId: String) = {
    val query = applications filter { x => x.appId === appId }

    executionContext from query selectFirstOpt
  }

  def updateApplication(appId:    String,
                        name:     String,
                        desc:     Option[String],
                        scope:    AuthorizationScope.ValueSet,
                        authType: AuthenticationType.Type) = {
    val query = applications filter { x =>
      x.appId === appId

    } map { x =>
      (x.name, x.scope, x.description, x.authType)
    }

    executionContext updateTo query value(name, scope, desc.orNull, authType) match {
      case e if e >= 0 =>
        log.debug(s"Update application success: App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")

      case e if e < 0 =>
        log.error(s"Update application failure with App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")
    }
  }

  def deleteApplication(appId: String) = {
    val query = applications filter { x => x.appId === appId }

//    executionContext from query delete match {
//      case e if e >= 0 =>
//        log.debug(s"Delete application success: App Id = ${appId}")
//
//      case e if e < 0 =>
//        log.error(s"Delete application failure ${query.deleteStatement} with App Id = ${appId}")
//    }
  }

  def blockApplication(appId: String) = {

    val query = applications filter {
      app => app.appId === appId
    } map {
      app => app.isActive
    }

    executionContext updateTo query value false match {
      case e if e >= 0  =>
        log.debug(s"Block application success: App Id = ${appId}")

      case e if e < 0  =>
        log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
    }
  }

  def unblockApplication(appId: String) = {

    val query = applications filter {
      app => app.appId === appId
    } map {
      app => app.isActive
    }

    executionContext updateTo query value true match {
      case e if e >= 0  =>
        log.debug(s"Block application success: App Id = ${appId}")

      case e if e < 0  =>
        log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
    }
  }

  def clear() = {
    val query = tokens filter { t => t.issueAt < expiredDateTime}

    executionContext delete query match {
      case e if e >= 0 =>
        log.debug(s"Expired tokens remove success: $e tokens was removed")

      case e if e < 0 =>
        log.error(s"Expired tokens remove failure: ${query.deleteStatement}")
    }
  }
}
