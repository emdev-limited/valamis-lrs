package com.arcusys.valamis.lrs.liferay.history

import com.arcusys.slick.migration.MigrationSeq
import com.arcusys.slick.migration.dialect.Dialect

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.meta.MTable

/**
 * Created by iliyatryapitsin on 16.04.15.
 */
trait BaseDbUpgrade {
  val database: JdbcBackend#Database
  val jdbcDriver: JdbcDriver

  implicit val dialect = Dialect(jdbcDriver) match {
    case Some(d) => d
    case None   => throw new NotImplementedError(s"Migration dialect for $jdbcDriver not implement")
  }

  def upgradeMigrations: MigrationSeq

  def downgradeMigrations: MigrationSeq

  def upgrade = database.withSession { implicit session =>
    upgradeMigrations.apply()
  }

  def downgrade = database.withSession { implicit session =>
    downgradeMigrations.apply()
  }

  def tables = database.withSession { implicit session =>
    jdbcDriver.defaultTables
  }
}
