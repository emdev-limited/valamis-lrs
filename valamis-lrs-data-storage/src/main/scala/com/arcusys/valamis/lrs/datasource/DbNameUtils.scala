package com.arcusys.valamis.lrs.datasource

import scala.slick.driver._


/**
 * Created by Iliya Tryapitsin on 03/03/15.
 */
object DbNameUtils {

  def tblName(str: String) = s"lrs_$str"

  def fkName(str: String) = s"fk_$str"

  def idxName(str: String) = s"idx_$str"

  def pkName(str: String) = s"pk_$str"

  // UUID not supported in Postgres < 4.3
  def uuidKeyLength = "char(36)"

  def varCharMax(implicit driver: JdbcDriver) = driver match {
      case driver: MySQLDriver => "text"
      case driver: PostgresDriver => "varchar(10485760)"
      case _ => "varchar(2147483647)"
    }

  def varCharPk(implicit driver: JdbcDriver) = driver match {
    case driver: MySQLDriver => "varchar(255)"
    case driver: PostgresDriver => "varchar(10485760)"
    case _ => "varchar(256)"
  }

  val category = "category"
  val grouping = "grouping"
  val other    = "other"
  val parent   = "parent"
}
