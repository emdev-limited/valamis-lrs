package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.row.{ActorRow, TokenRow}
import com.arcusys.valamis.lrs.jdbc.database._
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import org.joda.time.DateTime

import scala.slick.driver.JdbcDriver

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait TokenSchema {
  this: SecurityDataContext =>

  import driver.simple._
  import jodaSupport._
  import ForeignKeyAction._
  import DbNameUtils._

  class TokenTable(tag: Tag) extends Table[TokenRow](tag, tblName("tokens")) {

    def * = (userKey, appId, code, codeSecret, callback, issueAt, verifier, token, tokenSecret) <>
      (TokenRow.tupled, TokenRow.unapply)

    def userKey    = column[Option[ActorRow#Type]]("userId")
    def appId      = column[String]               ("appId")
    def code       = column[String]               ("code_")
    def codeSecret = column[String]               ("codeSecret")
    def callback   = column[String]               ("callback", O.Length(4000, varying = true))//TODO use another type for long string in Oracle
    def verifier   = column[Option[String]]       ("verifier")
    def token      = column[Option[String]]       ("token")
    def tokenSecret= column[Option[String]]       ("tokenSecret")
    def issueAt    = column[DateTime]             ("issueAt")

    def application = foreignKey("fk_token2application", appId, TableQuery[ApplicationTable])(x => x.appId, onUpdate = Restrict, onDelete = NoAction)

    def name_idx  = index("idx_token", (userKey, appId, token))
  }

  val tokens = TableQuery[TokenTable]
}
