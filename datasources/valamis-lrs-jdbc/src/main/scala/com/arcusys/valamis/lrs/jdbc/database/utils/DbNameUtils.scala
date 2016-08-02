package com.arcusys.valamis.lrs.jdbc.database.utils

import com.arcusys.slick.drivers.{OracleDriver, SQLServerDriver}
import scala.slick.ast.ColumnOption
import scala.slick.driver._

/**
 * Created by Iliya Tryapitsin on 03/03/15.
 */
object DbNameUtils {

  val NameSizeLimit = 30

  def checkLengthAndReturn(name: String) = {
    assert(name.length < NameSizeLimit, s"Name '$name' is too long: ${name.length}")
    name
  }

  def tblName(str: String) = checkLengthAndReturn(s"lrs_$str")

  def fkName(str: String) = checkLengthAndReturn(s"fk_$str")

  def idxName(str: String) = checkLengthAndReturn(s"idx_$str")

  def pkName(str: String) = checkLengthAndReturn(s"pk_$str")

  // UUID not supported in Postgres < 4.3
  def uuidKeyLength = "char(36)"

  def varCharMax(implicit driver: JdbcProfile) = driver match {
    case driver: MySQLDriver => "text"
    case driver: PostgresDriver => "varchar(10485760)"
    case driver: SQLServerDriver => "varchar(max)"
    case driver: OracleDriver => "varchar2(4000)"
    case _ => "varchar(2147483647)"
  }

  def varCharPk(implicit driver: JdbcDriver): String = varCharPk(None)

  def varCharPk(value: Option[String])(implicit driver: JdbcDriver): String = driver match {
    case driver: MySQLDriver => s"varchar(${ value getOrElse "254" })"
    case driver: PostgresDriver => s"varchar(${ value getOrElse "10485760" })"
    case _ => s"varchar(${ value getOrElse "256" })"
  }

  val category = "category"
  val grouping = "grouping"
  val other    = "other"
  val parent   = "parent"
}
