package com.arcusys.valamis.lrs.liferay.history.ver230

import com.arcusys.valamis.lrs.liferay.UpgradeProcess
import com.arcusys.valamis.lrs.liferay.history.SQLRunner
import com.arcusys.valamis.lrs.services.LRS

class DbUpgradeProcess extends UpgradeProcess with SQLRunner {
  override def getThreshold = 230
  val schema = new DbSchemaUpgrade(driver, db)

  override def doUpgrade() {
    logger.info("Upgrading to 2.3")

    val lrs = new LRS(driver, db)
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

  def areExistTables =
    doHasTable(schema.actors             .tableName) &&
    doHasTable(schema.scores             .tableName) &&
    doHasTable(schema.results            .tableName) &&
    doHasTable(schema.contexts           .tableName) &&
    doHasTable(schema.accounts           .tableName) &&
    doHasTable(schema.documents          .tableName) &&
    doHasTable(schema.activities         .tableName) &&
    doHasTable(schema.activities         .tableName) &&
    doHasTable(schema.statements         .tableName) &&
    doHasTable(schema.attachments        .tableName) &&
    doHasTable(schema.subStatements      .tableName) &&
    doHasTable(schema.stateProfiles      .tableName) &&
    doHasTable(schema.agentProfiles      .tableName) &&
    doHasTable(schema.activityProfiles   .tableName) &&
    doHasTable(schema.statementObjects   .tableName) &&
    doHasTable(schema.contextActivities  .tableName) &&
    doHasTable(schema.statementReferences.tableName)
}