package com.arcusys.valamis.lrs.jdbc.database

import com.arcusys.slick.drivers._

import scala.slick.driver._

/**
 * Created by Iliya Tryapitsin on 20/03/15.
 */
object SupportedDialect extends Enumeration{
  type Type = Value

  val MsSqlServer   = Value("sqlserver")
  val Oracle        = Value("oracle")
  val DB2           = Value("db2")
  val H2            = Value("h2")
  val Hsql          = Value("hsql")
  val MySql         = Value("mysql")
  val Postgres      = Value("postgres")

  def detectDialect(databaseProductName: String, majorVersion: Int) =  databaseProductName match {
    case name if isHsql(name)                      => HsqldbDriver
    case name if isMsSqlServer(name, majorVersion) => SQLServerDriver
    case name if isOracle(name, majorVersion)      => OracleDriver
    case name if isH2(name)                        => H2Driver
    case name if isMySql(name)                     => MySQLDriver
    case name if isPostgreSql(name)                => PostgresDriver
    case name if isDerby(name)                     => DerbyDriver
    case name if isAccess(name)                    => AccessDriver
    case name if isDB2(name, majorVersion)         => DB2Driver
    case name if isSybase(name, majorVersion)      => SybaseDriver
    case _                                         => throw new RuntimeException(s"Unsupport database: $databaseProductName $majorVersion")
  }

  //  else if (dbName.equals("Ingres") || dbName.equals("ingres") || dbName.equals("INGRES")) {
//    scala.slick.driver.
//  }
//  else if (dbName.equals("Sybase SQL Server")) {
//    scala.slick.driver.
//  }
//  else if (dbName.equals("Adaptive Server Enterprise")) {
//    scala.slick.driver.
//  }
//  else if (dbName.equals("Informix Dynamic Server")) {
//    scala.slick.driver.
//  }

  private def isAccess(databaseProductName: String) = databaseProductName.contains("Access")

  private def isPostgreSql(databaseProductName: String) = databaseProductName.equals("PostgreSQL")

  private def isDerby(databaseProductName: String) = databaseProductName.equals("Apache Derby")

  private def isMySql(databaseProductName: String) = databaseProductName.equals("MySQL")

  private def isH2(databaseProductName: String) = databaseProductName.equals("H2")

  private def isHsql(databaseProductName: String) = databaseProductName.startsWith("HSQL")

  private def isSybase(databaseProductName: String, majorVersion: Int) =
    databaseProductName.equals("ASE") && (majorVersion == 15)

  private def isDB2(databaseProductName: String, majorVersion: Int) =
    databaseProductName.startsWith("DB2") && (majorVersion >= 9)

  private def isOracle(databaseProductName: String, majorVersion: Int) =
    databaseProductName.startsWith("Oracle") && (majorVersion >= 10)

  private def isMsSqlServer(databaseProductName: String, majorVersion: Int) =
    databaseProductName.startsWith("Microsoft") && (majorVersion >= 9)
}
