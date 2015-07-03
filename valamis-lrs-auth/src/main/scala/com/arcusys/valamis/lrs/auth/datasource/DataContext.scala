package com.arcusys.valamis.lrs.auth.datasource

import com.arcusys.valamis.lrs.auth._
import com.arcusys.valamis.lrs.auth.datasource.row._
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import com.github.tototoshi.slick.GenericJodaSupport
import com.google.inject.Inject
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

class DataContext @Inject() (val driver :  JdbcDriver,
                             val database: JdbcBackend#Database) {
  protected lazy val logger = LoggerFactory.getLogger(getClass)
  protected val jodaSupport = new GenericJodaSupport(driver)

  import driver.simple._

  class ApplicationTable(tag: Tag) extends Table[Application](tag,"lrs_applications") with TypeMapper {

    def * = (appId, name, description, appSecret, scope, regDateTime, isActive, authType) <>
      (Application.tupled, Application.unapply)

    def appId       = column[String]  ("appId",      O.PrimaryKey)
    def name        = column[String]  ("name",       O.NotNull   )
    def description = column[String]  ("description",O.Nullable  )
    def appSecret   = column[String]  ("appSecret",  O.NotNull   )
    def regDateTime = column[DateTime]("regDateTime",O.NotNull/*, O.DBType("datetime")*/)
    def isActive    = column[Boolean] ("isActive",   O.NotNull, O.Default(true))
    def scope       = column[AuthorizationScope.ValueSet]("scope"   , O.NotNull)
    def authType    = column[AuthenticationType.Type]("authType", O.NotNull)

    def name_idx  = index("idx_app_name", name)
  }

  class TokenTable(tag: Tag) extends Table[Token](tag, "lrs_tokens") with TypeMapper {

    def * = (userKey.?, appId, code, codeSecret, callback, issueAt, verifier.?, token.?, tokenSecret.?) <>
      (Token.tupled, Token.unapply)

    def userKey    = column[Long]    ("userId",     O.Nullable)
    def appId      = column[String]  ("appId",      O.NotNull)
    def code       = column[String]  ("code_",      O.NotNull)
    def codeSecret = column[String]  ("codeSecret", O.NotNull)
    def callback   = column[String]  ("callback",   O.NotNull)
    def verifier   = column[String]  ("verifier",   O.Nullable)
    def token      = column[String]  ("token",      O.Nullable)
    def tokenSecret= column[String]  ("tokenSecret",O.Nullable)
    def issueAt    = column[DateTime]("issueAt",    O.Nullable/*, O.DBType("datetime")*/)

    def application = foreignKey("fk_token2application", appId, TableQuery[ApplicationTable])(x => x.appId, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.NoAction)

    def name_idx  = index("idx_token", (userKey, appId, token))
  }

  class UserApplicationAccessTable(tag: Tag) extends Table[UserApplicationAccess](tag,"lrs_user_app_access") with TypeMapper {

    def * = (userId, applicationKey, isAllow, createDateTime, updateDateTime) <>
      (UserApplicationAccess.tupled, UserApplicationAccess.unapply)

    def userId         = column[Long]    ("userId",  O.NotNull)
    def applicationKey = column[String]  ("appId",   O.NotNull)
    def isAllow        = column[Boolean] ("isAllow", O.NotNull)
    def createDateTime = column[DateTime]("created", O.NotNull/*, O.DBType("datetime")*/)
    def updateDateTime = column[DateTime]("updated", O.NotNull/*, O.DBType("datetime")*/)

    def name_idx  = index("idx_user_app_access", (userId, applicationKey), unique = true)
  }

  val applications = TableQuery[ApplicationTable]
  val tokens       = TableQuery[TokenTable]
  val userApps     = TableQuery[UserApplicationAccessTable]
}

trait DataQuery {
  this: DataContext =>

  import driver.simple._

  def getAppScope(appId: String)(implicit session: JdbcBackend#Session) = applications
    .filter { x => x.appId === appId }
    .map { app => app.scope }
    .firstOption

  def getApp(appId: String)(implicit session: JdbcBackend#Session) = applications
    .filter { x => x.appId === appId }
    .firstOption
}