package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.row.ApplicationRow
import com.arcusys.valamis.lrs.jdbc.database._
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils._
import com.arcusys.valamis.lrs.security.AuthenticationType
import com.arcusys.valamis.lrs.tincan.AuthorizationScope
import org.joda.time.DateTime

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait ApplicationSchema {
  this: SecurityDataContext =>

  import driver.simple._
  import jodaSupport._

  class ApplicationTable(tag: Tag) extends Table[ApplicationRow](tag,tblName("applications")) {

    def * = (appId, name, description, appSecret, scope, regDateTime, isActive, authType) <>
      (ApplicationRow.tupled, ApplicationRow.unapply)

    def appId       = column[String]  ("appId",      O.PrimaryKey)
    def name        = column[String]  ("name",       O.NotNull   )
    def description = column[String]  ("description",O.Nullable  )
    def appSecret   = column[String]  ("appSecret",  O.NotNull   )
    def regDateTime = column[DateTime]("regDateTime",O.NotNull   )
    def isActive    = column[Boolean] ("isActive",   O.NotNull, O.Default(true))
    def scope       = column[AuthorizationScope.ValueSet]("scope"   , O.NotNull)
    def authType    = column[AuthenticationType.Type]    ("authType", O.NotNull)

    def name_idx  = index("idx_app_name", name)
  }

  val applications = TableQuery[ApplicationTable]
}
