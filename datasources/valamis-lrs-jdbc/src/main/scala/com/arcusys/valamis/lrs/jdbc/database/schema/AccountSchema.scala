package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row.AccountRow

import scala.slick.lifted.ProvenShape

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait AccountSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class AccountsTable(tag: Tag) extends LongKeyTable[AccountRow](tag, tblName("accounts")) {

    private type Maybe = (?[AccountRow#Type], ?[String], ?[String])

    def * = (key.?, homepage, name) <> (AccountRow.tupled, AccountRow.unapply)

    def ? = ProvenShape.proveShapeOf((key.?, homepage.?, name.?)) .<> [?[AccountRow], Maybe] (applyMaybe, unapplyBlank)

    val unapplyBlank = (_:  ?[AccountRow]) => None
    val applyMaybe   = (t: (?[AccountRow#Type], ?[String], ?[String])) => t match {
      case (Some(id), Some(homepage), Some(name)) => Some(AccountRow(Some(id), homepage, name))
      case _ => None
    }

    def homepage = column [String] ("homePage", O.DBType (varCharMax))
    def name     = column [String] ("name"    , O.DBType (varCharMax))

    //def indx = index (idxName("account"), key) this one fails on Oracle, because
    //primary key is already indexed; also, it's useless to create index on primary key in most RDBMS,
    //because, it's usually automatically indexed by RDBMS itself
  }

  lazy val accounts = TQ[AccountsTable]
}
