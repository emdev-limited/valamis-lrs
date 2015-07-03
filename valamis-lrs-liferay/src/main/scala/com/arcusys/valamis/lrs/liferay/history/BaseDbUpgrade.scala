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
  val db: JdbcBackend#Database
  val driver: JdbcDriver

  implicit val dialect = Dialect(driver) match {
    case Some(d) => d
    case None   => throw new NotImplementedError(s"Migration dialect for $driver not implement")
  }

  def upgradeMigrations: MigrationSeq

  def downgradeMigrations: MigrationSeq

  def upgrade = db.withSession { implicit session =>
    upgradeMigrations.apply()
  }

  def downgrade = db.withSession { implicit session =>
    downgradeMigrations.apply()
  }

  def tables = db.withSession { implicit session =>
    driver.defaultTables
  }
}
