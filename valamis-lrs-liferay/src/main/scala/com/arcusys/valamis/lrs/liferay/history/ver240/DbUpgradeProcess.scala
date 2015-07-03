package com.arcusys.valamis.lrs.liferay.history.ver240

import com.arcusys.valamis.lrs.liferay.{Loggable, UpgradeProcess}
import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.arcusys.valamis.lrs.services.LRS

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import scala.tools.nsc.interpreter.Logger

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 240
  val schema = new DbSchemaUpgrade(driver, db)

  override def doUpgrade() {
    logger.info("Upgrading to 2.4")
    val schema = new DbSchemaUpgrade(driver, db)

    if(logger.isDebugEnabled)
      logger.debug(schema.upgradeMigrations.migrations.mkString(";\n"))

    if(schema.hasNotTables) {
      logger.info("Applying database schema changes")
      schema.upgrade
    } else {
      logger.info("Tables for version 2.4 exists already")
      
      val lrs = new LRS(driver, db)
      val dataMigration = new DataUpgrade(lrs)
      dataMigration.upgrade
    }
  }
}

