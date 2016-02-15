package com.arcusys.valamis.lrs.liferay.util

import com.arcusys.slick.drivers._
import scala.slick.driver._


/**
 * Created by Iliya Tryapitsin on 18.05.15.
 */
class SqlAccessGenerator {
  val dialects = Seq(
    H2Driver,
    HsqldbDriver,
    SQLServerDriver,
    OracleDriver,
    MySQLDriver,
    PostgresDriver,
    DerbyDriver,
    DB2Driver,
    SybaseDriver
  )

  val identifierQuotes = Seq("", """"""", "`", "[]")

  def sqlStatements: Unit = {
    val rawSql = dialects.flatMap { dialect =>
      val upgradeSql = new DbUpgradeSql(dialect, null, null)
      upgradeSql.sql
    }
      .distinct
      .mkString(",\\" + "\n")
    println(rawSql)
    println("Sql queries generated")
  }

  def sqlTables: Unit = {
    val rawSql = identifierQuotes.flatMap { identifierQuote =>
      val upgradeSql = new DbUpgradeSql(H2Driver, null, null)
      identifierQuote.length match {
        case 0 => upgradeSql.tables
        case 1 => upgradeSql.tables.map { t => s"$identifierQuote$t$identifierQuote" }
        case 2 => upgradeSql.tables.map { t => s"${identifierQuote.charAt(0)}$t${identifierQuote.charAt(1)}" }
      }
    }
      .distinct
      .sortBy { t => t }
      .mkString(",\\" + "\n")

    println(rawSql)
    println("Sql tables generated")
  }
}
