package com.arcusys.valamis.lrs.jdbc.history.ver250

import javax.inject.Inject

import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.SimpleJodaSupport
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import org.apache.commons.logging.Log

/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade @Inject() (val jdbcDriver: JdbcDriver,
                                 val database: JdbcBackend#Database,
                                 val lrs: Lrs,
                                 val logger: Log) extends BaseDbUpgrade {
  val jodaSupport      = new SimpleJodaSupport(jdbcDriver)
  val authDataContext  = new JdbcSecurityManager(jdbcDriver, database)

  val tokens = authDataContext.tokens.baseTableRow

  def upgradeMigrations = MigrationSeq(
    tokens.alterColumnType(_.callback)
  )


  def downgradeMigrations = throw new Exception("Can't apply migration 'cause it contains irreversible steps")

  override def up(lrs: Lrs): Unit = {
    logger.info("Upgrading to 2.5")

    if(logger.isDebugEnabled)
      logger.debug(upgradeMigrations.migrations.mkString(";\n"))


    logger.info("Applying database schema changes")
    upgrade
  }

  override def down(lrs: Lrs): Unit = ()
}