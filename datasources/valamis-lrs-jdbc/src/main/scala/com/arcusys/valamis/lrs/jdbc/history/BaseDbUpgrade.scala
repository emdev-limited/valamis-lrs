package com.arcusys.valamis.lrs.jdbc.history

import java.sql.SQLException

import com.arcusys.slick.migration.MigrationSeq
import com.arcusys.slick.migration.dialect.Dialect
import com.arcusys.valamis.lrs.DbUpgrade
import org.apache.commons.logging.Log

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.meta.MTable
import scala.slick.jdbc.{JdbcBackend, StaticQuery}

/**
  * Created by iliyatryapitsin on 16.04.15.
  *
  */
trait BaseDbUpgrade extends DbUpgrade {
  val database: JdbcBackend#Database
  val jdbcDriver: JdbcDriver

  def logger: Log

  implicit val dialect = Dialect(jdbcDriver) getOrElse (throw new NotImplementedError(s"Migration dialect for $jdbcDriver not implement"))

  def upgradeMigrations: MigrationSeq

  def downgradeMigrations: MigrationSeq

  def upgrade: Unit = database.withSession { implicit session =>
    upgradeMigrations.apply()
  }

  def downgrade: Unit = database.withSession { implicit session =>
    downgradeMigrations.apply()
  }

  def tables: Seq[MTable] = database.withSession { implicit session =>
    jdbcDriver.defaultTables
  }

  def hasTableInOracle(tableName: String): Boolean = {
    database.withSession { implicit s =>
      try {
        StaticQuery.queryNA[String](s"SELECT * FROM $tableName WHERE 1 = 0").list
        true
      } catch {
        case _: SQLException => false
      }
    }
  }

}
