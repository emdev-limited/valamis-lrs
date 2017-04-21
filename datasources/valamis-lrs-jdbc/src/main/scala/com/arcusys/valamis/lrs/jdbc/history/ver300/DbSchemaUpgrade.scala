package com.arcusys.valamis.lrs.jdbc.history.ver300

import javax.inject.Inject

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import com.arcusys.valamis.lrs.jdbc.JdbcLrs

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

class DbSchemaUpgrade @Inject()(val jdbcDriver: JdbcDriver,
                                val database: JdbcBackend#Database,
                                val lrs: Lrs) extends BaseDbUpgrade {
  val dataContext = lrs.asInstanceOf[JdbcLrs]

  val contextActivityActivity = dataContext.contextActivitiesActivity baseTableRow
  val contextActivityContext = dataContext.contextActivitiesContext baseTableRow
  val contextActivity = dataContext.contextActivities baseTableRow


  val tablesInMigration = Seq(
    contextActivity.tableName
  ).map { name =>
    caseForDriver(name)
  }

  val tablesInMigrationNew = Seq(
    contextActivityContext.tableName,
    contextActivityActivity.tableName
  ).map { name =>
    caseForDriver(name)
  }

  def caseForDriver(name: String): String = {
    jdbcDriver match {
      case OracleDriver => name.toUpperCase()
      case _ => name
    }
  }

  def hasNotTables(tablesSeq: Seq[String]) = tables
    .map { t => t.name.name }
    .intersect {
      tablesSeq
    }
    .isEmpty

  def downgradeMigrations = throw new Exception("Can't apply migration 'cause it contains irreversible steps")


  def upgradeMigrations =
      contextActivityActivity.create.addColumns &
      contextActivityContext.create.addColumns &
      contextActivityActivity.addPrimaryKeys &
      contextActivityContext.addPrimaryKeys &
      contextActivityActivity.addForeignKeys &
      contextActivityContext.addForeignKeys &
      contextActivityActivity.addIndexes &
      contextActivityContext.addIndexes


  def up(lrs: Lrs): Unit = {

    logger.info("Upgrading to 3.0")

    if (logger.isDebugEnabled)
      logger.debug(upgradeMigrations.migrations.mkString(";\n"))

    if (!hasNotTables(tablesInMigration) && hasNotTables(tablesInMigrationNew)) {
      logger.info("Applying database schema changes")
      upgrade

      logger.info("Applying data update")
      val dataMigration = new DataUpgrade(dataContext, jdbcDriver, database)
      dataMigration.upgrade
    }
    else logger.info("Tables for version 3.0 exists already")

  }

  def down(lrs: Lrs): Unit = ()
}

