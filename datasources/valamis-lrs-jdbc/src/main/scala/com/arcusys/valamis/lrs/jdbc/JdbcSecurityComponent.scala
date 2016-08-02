package com.arcusys.valamis.lrs.jdbc

import java.util.UUID

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.SecurityDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.{TokenRow, ApplicationRow}
import com.arcusys.valamis.lrs.security.{Application, Token, AuthenticationStatus, AuthenticationType}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.slick.jdbc.JdbcBackend

trait JdbcSecurityComponent extends SecurityComponent with SecurityDataContext {

  import driver.simple._
  import jodaSupport._

  val log = LoggerFactory.getLogger("com.arcusys")

  lazy val applicationStorage: ApplicationStorage = new JdbcApplicationStorage
  lazy val tokenStorage: TokenStorage = new JdbcTokenStorage

  class JdbcApplicationStorage extends ApplicationStorage {

    override def getAppScope(appId: String): Option[AuthorizationScope.ValueSet] = {
      val query = applications filter {
        x => x.appId === appId
      } map {
        app => app.scope
      }

      db withSession { implicit s =>
        query.firstOption
      }
    }

    override def getApplications(count: Int, offset: Int): Seq[Application] =  {
      var query = applications drop offset
      if(count > 0)
        query = query take count

      db withSession { implicit s =>
        query.run
      } map { _.convert }
    }

    override def registrationApp(appName:        String,
                        appDescription: Option[String],
                        scope:          AuthorizationScope.ValueSet,
                        authType:       AuthenticationType.Type): Option[Application] = {
      val app = ApplicationRow(
        appId = UUID.randomUUID.toString,
        name = appName,
        description = appDescription.orNull,
        appSecret = UUID.randomUUID.toString,
        scope = scope,
        regDateTime = DateTime.now,
        authType = authType
      )

      db withSession { implicit s =>
        applications += app match {
          case e if e >= 0 =>
            log.debug(s"Registered application success: App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
            app.convert ?

          case e if e < 0 =>
            log.error(s"Registered application failure: ${applications.insertStatement} with App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
            None
        }
      }
    }

    override def getApplication(appId: String): Option[Application] = {
      val query = applications filter { x => x.appId === appId }

      db withSession { implicit s =>
        query.firstOption
      } map { _.convert }
    }

    override def updateApplication(appId:    String,
                          name:     String,
                          desc:     Option[String],
                          scope:    AuthorizationScope.ValueSet,
                          authType: AuthenticationType.Type): Unit = {
      val query = applications filter { x =>
        x.appId === appId

      } map { x =>
        (x.name, x.scope, x.description, x.authType)
      }

      db withSession { implicit s =>
        query update(name, scope, desc.orNull, authType) match {
          case e if e >= 0 =>
            log.debug(s"Update application success: App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")

          case e if e < 0 =>
            log.error(s"Update application failure with App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")
        }
      }
    }

    override def deleteApplication(appId: String): Unit = {
      val query = applications filter { x => x.appId === appId }

      db withSession { implicit s =>
        query.delete match {
          case e if e >= 0 =>
            log.debug(s"Delete application success: App Id = ${appId}")

          case e if e < 0 =>
            log.error(s"Delete application failure ${query.deleteStatement} with App Id = ${appId}")
        }
      }
    }

    override def blockApplication(appId: String): Unit = {

      val query = applications filter {
        app => app.appId === appId
      } map {
        app => app.isActive
      }

      db withSession { implicit s =>
        query update false match {
          case e if e >= 0 =>
            log.debug(s"Block application success: App Id = ${appId}")

          case e if e < 0 =>
            log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
        }
      }
    }

    override def unblockApplication(appId: String): Unit = {

      val query = applications filter {
        app => app.appId === appId
      } map {
        app => app.isActive
      }

      db withSession { implicit s =>
        query update true match {
          case e if e >= 0 =>
            log.debug(s"Block application success: App Id = ${appId}")

          case e if e < 0 =>
            log.error(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
        }
      }
    }

    override def checkByBasic(clientId: String, secret: String): Boolean = {
      val query = applications filter { x =>
        (x.appId === clientId) && (x.appSecret === secret)
      }

      db withSession { implicit s => query.firstOption.isDefined }
    }
  }

  class JdbcTokenStorage extends TokenStorage {


    override def getCallback(token: String): Option[String] = {
      val query = tokens filter { x => x.code === token } map { x => x.callback }

      db withSession { implicit s =>
        query.firstOption
      }
    }


    override def checkByToken(appId: String, token: String): Boolean = {

        val query = tokens filter { t =>
          (t.appId === appId) &&
            (t.token === token) &&
            (t.issueAt > expiredDateTime)
        }

        db withSession { implicit s => query.firstOption.isDefined }
      }

    override def setAuthorized(appId: String,
                      requestCode: String,
                      verified: String): Unit = {
      val query = tokens filter { t =>
        (t.appId === appId) && (t.code === requestCode)

      } map { t => t.verifier }

      db withSession { implicit s => query update verified.toOption match {
        case e if e >= 0 =>
          log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode")

        case e if e < 0 =>
          log.error(s"Set access token failure: ${query.updateStatement} with User Id = None, App Id = $appId, Request Code = $requestCode")
      }
      }
    }

    override def setAccessToken(appId: String,
                       requestCode: String,
                       accessCode: String,
                       accessSecret: String) = {

      val query = tokens filter {
        t => (t.appId === appId) && (t.code === requestCode)
      } map { t => (t.issueAt, t.token, t.tokenSecret) }

      db withSession { implicit s =>
        query update(DateTime.now, accessCode.toOption, accessSecret.toOption) match {
          case e if e >= 0 =>
            log.debug(s"Set access token success: User Id = None, App Id = $appId, Access Code = $accessCode")

          case e if e < 0 =>
            log.error(s"Set access token failure: ${tokens.updateStatement} with User Id = None, App Id = $appId, Access Code = $accessCode")
        }
      }
    }

    override def setRequestToken(appId: String,
                        requestCode: String,
                        requestSecret: String,
                        callback: String): Option[Token] = {
      val token = TokenRow(None, appId, requestCode, requestSecret, callback, DateTime.now)

      db withSession { implicit s =>
        tokens += token match {
          case e if e >= 0 =>
            log.debug(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode, callback = $callback")
            Option(token.convert)

          case e if e < 0 =>
            log.error(s"Set access token failure: ${tokens.insertStatement} with User Id = None, App Id = $appId, Access Code = $requestCode")
            None
        }
      }
    }

    override def getRequestToken(appId: String,
                        requestCode: String): Option[Token] = {
      val query = tokens
        .filter(t => t.code === requestCode)
        .filter(t => t.token.isEmpty)
        .filter(t => t.issueAt > expiredDateTime)

      db withSession { implicit s =>
        query.firstOption
      } map { _.convert }
    }

    override def getAccessToken(appId: String,
                       token: String): Option[Token] = {
      val query = tokens
        .filter(t => t.appId === appId)
        .filter(t => t.token === token)
        .filter(t => t.issueAt > expiredDateTime)

      db withSession { implicit s =>
        query.firstOption
      } map { _.convert }
    }


    override def clear(): Unit = {
      val query = tokens filter { t => t.issueAt < expiredDateTime }

      db withSession { implicit s =>
        query.delete match {
          case e if e >= 0 =>
            log.debug(s"Expired tokens remove success: $e tokens was removed")

          case e if e < 0 =>
            log.error(s"Expired tokens remove failure: ${query.deleteStatement}")
        }
      }
    }
  }
}
