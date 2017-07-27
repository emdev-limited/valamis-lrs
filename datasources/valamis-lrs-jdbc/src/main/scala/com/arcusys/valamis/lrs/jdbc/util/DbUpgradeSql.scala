package com.arcusys.valamis.lrs.jdbc.util

import com.arcusys.valamis.lrs.jdbc.{JdbcLrs, Loggable}
import com.arcusys.valamis.lrs.jdbc.history.ver230.{DbSchemaUpgrade => v230}
import com.arcusys.valamis.lrs.jdbc.history.ver240.{DbSchemaUpgrade => v240}
import org.apache.commons.logging.impl.NoOpLog

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 18.05.15.
 */
class DbUpgradeSql(val driver:  JdbcDriver,
                   val db:      JdbcBackend#Database,
                   val jdbcLrs: JdbcLrs) extends Loggable {
  val schema230 = new v230(driver, db, jdbcLrs, new NoOpLog)
  val schema240 = new v240(driver, db, jdbcLrs, new NoOpLog)

  def sql: Seq[String] = (schema230.upgradeMigrations.migrations ++ schema240.upgradeMigrations.migrations)
    .map { migration => migration.toString.replace("\n", "") }
    .filter { sql => !sql.isEmpty }
    .distinct

  def tables: Seq[String] = (schema230.tablesInMigration ++ schema240.tablesInMigration)
    .distinct
}
