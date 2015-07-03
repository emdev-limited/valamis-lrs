package com.arcusys.valamis.lrs.auth

import java.util.UUID
import javax.inject.Inject

import akka.actor._
import com.arcusys.valamis.lrs.{NamedActor, _}
import com.arcusys.valamis.lrs.auth.datasource.{DataContext, DataQuery}
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 24.04.15.
 */
object AuthenticationActor extends NamedActor {

  case class GetApplications(take: Int, offset: Int)

  case class GetApplication(appId: String)

  case class DeleteApplication(appId: String)

  case class CheckByBasic(clientId: String,
                          secret: String,
                          scopeRequest: AuthorizationScope.ValueSet)

  case class CheckByToken(appId: String,
                          token: String,
                          scopeRequest: AuthorizationScope.ValueSet)

  case class RegistrationApp(appName: String,
                             appDescription: Option[String],
                             scope: AuthorizationScope.ValueSet,
                             authType: AuthenticationType.Type)

  case class UpdateApplication(appId: String,
                               appName: String,
                               appDescription: Option[String],
                               scope: AuthorizationScope.ValueSet,
                               authType: AuthenticationType.Type)

  case class BlockApplication(appId: String)

  case class UnblockApplication(appId: String)

  case class SetAccessToken(appId: String,
                            requestCode: String,
                            accessCode: String,
                            accessSecret: String)

  case class SetAuthorized(appId: String, requestCode: String, verified: String)

  case class SetRequestToken(appId: String,
                             requestCode: String,
                             requestSecret: String,
                             callback: String)

  case class GetRequestToken(appId: String,
                             requestCode: String)

  case class GetCallback(token: String)

  case class GetAccessToken(appId: String,
                            token: String)

  case class Clear()

  val expiredPeriod = 60 * 60 * 24

  // One day
  def expiredDateTime = DateTime.now.minusSeconds(expiredPeriod)

  override def name: String = "AuthenticationActor"
}

class AuthenticationActor @Inject()(dr:  JdbcDriver,
                                    db: JdbcBackend#Database)
  extends DataContext(dr, db)
  with DataQuery
  with Actor
  with ActorLogging {

  import AuthenticationActor._
  import driver.simple._
  import jodaSupport._

  override def receive: Receive = {
    case GetCallback(token: String) =>
      sender ! database.withSession { implicit session =>
        tokens
          .filter { x => x.code === token }
          .map { x => x.callback }
          .firstOption
      }

    case GetApplications(take, offset) =>
      sender ! database.withSession { implicit session =>
        applications
          .take(take)
          .list
          .drop(offset)
      }

    case CheckByToken(appId, token, scopeRequest) =>
      sender ! database.withSession { implicit s =>

        log.info(s"Check by token for app Id: $appId")

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

    case CheckByBasic(appId, appSecret, scopeRequest) =>
      sender ! database.withSession { implicit s =>

        log.info(s"Check by basic for app Id: $appId")

        getAppScope(appId) match {
          case None        => AuthenticationStatus.Denied
          case Some(scope) =>
            val accessRightSuccess = scope <== scopeRequest

            val valid = applications
              .filter(x => x.appId === appId)
              .filter(x => x.appSecret === appSecret)
              .firstOption.isDefined

            AuthenticationStatus(valid && accessRightSuccess)
        }
      }

    case SetAuthorized(appId, requestCode, verified) =>
      database.withSession { implicit s =>

        tokens
          .filter(t => t.appId === appId)
          .filter(t => t.code === requestCode)
          .map(t => t.verifier)
          .update(verified) match {
            case e if e >= 0 =>
              log.info(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode")

            case e if e < 0  =>
              log.info(s"Set access token failure: ${tokens.updateStatement} with" +
                s" User Id = None, App Id = $appId, Request Code = $requestCode")
          }
      }

    case SetAccessToken(appId, requestCode, accessCode, accessSecret) =>
      database.withSession { implicit session =>

        tokens
          .filter(t => t.appId === appId && t.code === requestCode)
          .map(t => (t.issueAt, t.token, t.tokenSecret))
          .update((DateTime.now, accessCode, accessSecret)) match {
          case e if e >= 0 =>
            log.info(s"Set access token success: User Id = None, App Id = $appId, Access Code = $accessCode")

          case e if e < 0  =>
            log.info(s"Set access token failure: ${tokens.updateStatement} with User Id = None, App Id = $appId, Access Code = $accessCode")
        }
      }

    case SetRequestToken(appId, requestCode, requestSecret, callback) =>
      database.withSession { implicit session =>

        val token = Token(None, appId, requestCode, requestSecret, callback, DateTime.now())
        tokens.insert(token) match {
          case e if e >= 0 =>
            log.info(s"Set access token success: User Id = None, App Id = $appId, Request Code = $requestCode, callback = $callback")

          case e if e < 0  =>
            log.info(s"Set access token failure: ${tokens.insertStatement} with User Id = None, App Id = $appId, Access Code = $requestCode")
        }
      }

    case GetRequestToken(appId, requestCode) =>
      sender ! database.withSession { implicit session =>

        log.info(s"Get request token for app Id: $appId")

        val token = tokens
          .filter(t => t.code === requestCode)
          .filter(t => t.token.isNull)
          .filter(t => t.issueAt > expiredDateTime)
          .firstOption

        token
      }

    case GetAccessToken(appId, token) =>
      sender ! database.withSession { implicit session =>

        val foundToken = tokens
          .filter(t => t.appId === appId)
          .filter(t => t.token === token)
          .filter(t => t.issueAt > expiredDateTime)
          .firstOption

        foundToken
      }

    case RegistrationApp(name, desc, scope, aType) =>
      sender ! database.withSession { implicit session =>
        val app = Application(
          appId = UUID.randomUUID.toString,
          name = name,
          description = desc.orNull,
          appSecret = UUID.randomUUID.toString,
          scope = scope,
          regDateTime = DateTime.now,
          authType = aType
        )
        applications.insert(app) match {
          case e if e >= 0 =>
            log.info(s"Registered application success: App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
            app.toOption

          case e if e < 0  =>
            log.info(s"Registered application failure: ${applications.insertStatement} with " +
              s"App Id = ${app.appId}, Name = ${app.name}, App Secret = ${app.appSecret}")
            None
        }
      }

    case GetApplication(appId) =>
      sender ! database.withSession { implicit session =>
        getApp(appId)
      }

    case UpdateApplication(appId, name, desc, scope, authType) =>
      database.withSession { implicit session =>
        val query = applications
          .filter(x => x.appId === appId)
          .map(x => (x.name, x.scope, x.description, x.authType))

        query.update((name, scope, desc.orNull, authType)) match {
          case e if e >= 0 =>
            log.info(s"Update application success: App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")

          case e if e < 0  =>
            log.info(s"Update application failure: ${query.updateStatement} with " +
              s"App Id = $appId, Name = $name, Desc = $desc, Scope = $scope")
        }
      }

    case DeleteApplication(appId) =>
      database.withSession { implicit session =>
        val query = applications.filter(x => x.appId === appId)

        log.info(s"Delete application for app Id: $appId")

        query.delete match {
          case e if e >= 0 =>
            log.info(s"Delete application success: App Id = ${appId}")

          case e if e < 0  =>
            log.info(s"Delete application failure: ${query.deleteStatement} with App Id = ${appId}")
        }
      }

    case BlockApplication(appId) =>
      database.withSession { implicit session =>
        val query = applications
          .filter { app => app.appId === appId }
          .map { app => app.isActive }

        query.update(false) match {
          case e if e >= 0  =>
            log.info(s"Block application success: App Id = ${appId}")

          case e if e < 0  =>
            log.info(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
        }
      }

    case UnblockApplication(appId) =>
      database.withSession { implicit session =>
        val query = applications
          .filter { app => app.appId === appId }
          .map { app => app.isActive }

        query.update(true) match {
          case e if e >= 0 =>
            log.info(s"Block application success: App Id = ${appId}")

          case e if e < 0  =>
            log.info(s"Block application failure: ${query.updateStatement} with App Id = ${appId}")
        }
      }

    case Clear() =>
      database.withSession { implicit session =>

        val query = tokens
          .filter { t => t.issueAt < expiredDateTime }

        query.delete match {
          case e if e >= 0 =>
            log.info(s"Expired tokens remove success: ${e} tokens was removed")

          case e if e < 0 =>
            log.info(s"Expired tokens remove failure: ${query.deleteStatement}")
        }
      }

    case DeadLetter(msg, from, to) => print("Dead letter")
  }
}