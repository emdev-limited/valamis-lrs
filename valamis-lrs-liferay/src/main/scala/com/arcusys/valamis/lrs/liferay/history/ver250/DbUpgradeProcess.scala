package com.arcusys.valamis.lrs.liferay.history.ver250

import com.arcusys.valamis.lrs.liferay.UpgradeProcess
import com.arcusys.valamis.lrs.liferay.history.SQLRunner

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 250
  val schema = new DbSchemaUpgrade(driver, db)

  override def doUpgrade() {
    logger.info("Upgrading to 2.5")

    if(logger.isDebugEnabled)
      logger.debug(schema.upgradeMigrations.migrations.mkString(";\n"))


    logger.info("Applying database schema changes")
    schema.upgrade
  }
}

