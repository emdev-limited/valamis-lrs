package com.arcusys.valamis.lrs.liferay.history.ver230

import com.arcusys.valamis.lrs.liferay.UpgradeProcess
import com.arcusys.valamis.lrs.liferay.history.SQLRunner

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 230
  val schema = new DbSchemaUpgrade(driver, db, lrs)

  override def doUpgrade() {
    logger.info("Upgrading to 2.3")

    if(logger.isDebugEnabled)
      logger.debug(schema.upgradeMigrations.migrations.mkString(";\n"))

    if(schema.hasNotTables) {
      logger.info("Applying database schema changes")
      schema.upgrade
    }
    else logger.info("Tables for version 2.3 exists already")

    val dataMigration = new DataUpgrade(lrs)
    logger.info("Applying data changes")
    dataMigration.upgrade
  }
}