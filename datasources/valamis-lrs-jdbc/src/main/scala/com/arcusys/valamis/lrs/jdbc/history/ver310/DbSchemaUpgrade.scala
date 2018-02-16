package com.arcusys.valamis.lrs.jdbc.history.ver310

import javax.inject.Inject

import com.arcusys.slick.migration._
import com.arcusys.slick.migration.table.TableMigration
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import org.apache.commons.logging.Log

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

class DbSchemaUpgrade @Inject()(val jdbcDriver: JdbcDriver,
                                val database: JdbcBackend#Database,
                                val lrs: Lrs,
                                val logger: Log) extends BaseDbUpgrade {
  val dataContext = lrs.asInstanceOf[JdbcLrs]

  import jdbcDriver.simple._

  val activityMigration = TableMigration(dataContext.activities)

  def downgradeMigrations = throw new Exception("Can't apply migration 'cause it contains irreversible steps")

  def upgradeMigrations = MigrationSeq(
    activityMigration.alterColumnNulls(_.column[String]("id", jdbcDriver.columnOptions.NotNull))
  )

  def up(lrs: Lrs): Unit = {
    logger.info("Upgrading to 3.1")

    if (logger.isDebugEnabled)
      logger.debug(upgradeMigrations.migrations.mkString(";\n"))

    logger.info("Applying data update")
    val dataMigration = new DataUpgrade(dataContext, jdbcDriver, database)
    dataMigration.upgrade

    logger.info("Applying database schema changes")

    try upgrade catch {
      case e: MigrationException => logger.warn(e.getMessage)
      case e: Exception => logger.error("Couldn't migrate schema up to 3.1 version", e)
    }
  }

  def down(lrs: Lrs): Unit = ()
}

