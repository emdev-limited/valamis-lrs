package com.arcusys.valamis.lrs.jdbc.history.ver230

import javax.inject.Inject

import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils._

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import com.arcusys.slick.drivers.OracleDriver
import org.apache.commons.logging.Log

import scala.language.postfixOps

/**
  * Created by Iliya Tryapitsin on 20/01/15.
  *
  */
class DbSchemaUpgrade @Inject()(val jdbcDriver: JdbcDriver,
                                val database: JdbcBackend#Database,
                                val lrs: Lrs,
                                val logger: Log) extends BaseDbUpgrade {
  val dataContext = lrs.asInstanceOf[JdbcLrs]

  val actors              = dataContext.actors              baseTableRow
  val scores              = dataContext.scores              baseTableRow
  val results             = dataContext.results             baseTableRow
  val accounts            = dataContext.accounts            baseTableRow
  val contexts            = dataContext.contexts            baseTableRow
  val documents           = dataContext.documents           baseTableRow
  val activities          = dataContext.activities          baseTableRow
  val statements          = dataContext.statements          baseTableRow
  val attachments         = dataContext.attachments         baseTableRow
  val subStatements       = dataContext.subStatements       baseTableRow
  val agentProfiles       = dataContext.agentProfiles       baseTableRow
  val stateProfiles       = dataContext.stateProfiles       baseTableRow
  val activityProfiles    = dataContext.activityProfiles    baseTableRow
  val statementObjects    = dataContext.statementObjects    baseTableRow
  val contextActivities   = dataContext.contextActivities   baseTableRow
  val statementReferences = dataContext.statementReferences baseTableRow

  val tablesInMigration = Seq(
    actors             .tableName,
    scores             .tableName,
    results            .tableName,
    accounts           .tableName,
    contexts           .tableName,
    documents          .tableName,
    activities         .tableName,
    statements         .tableName,
    attachments        .tableName,
    subStatements      .tableName,
    agentProfiles      .tableName,
    stateProfiles      .tableName,
    activityProfiles   .tableName,
    statementObjects   .tableName,
    contextActivities  .tableName,
    statementReferences.tableName
  ).map { name =>
    jdbcDriver match {
      case OracleDriver => name.toUpperCase()
      case _ => name
    }
  }

  private def hasNotTables = {
    jdbcDriver match {
      case OracleDriver => !(tablesInMigration exists hasTableInOracle)
      case _ =>
        tables map { t => t.name.name } intersect tablesInMigration isEmpty
    }
  }

  def downgradeMigrations =
    dropForeignKey(fkName("activityProfiles2document"), tblName("activityProfiles")) &
    dropForeignKey(fkName("activityProfile2activity" ), tblName("activityProfiles")) &
    dropForeignKey(fkName("stateProfiles2document"   ), tblName("stateProfiles"   )) &
    dropForeignKey(fkName("stateProfile2activity"    ), tblName("stateProfiles" )) &
    dropForeignKey(fkName("agentProfile2document"    ), tblName("agentProfiles" )) &
    dropForeignKey(fkName("stateProfile2agent"       ), tblName("stateProfiles" )) &
    dropForeignKey(fkName("agentProfile2agent"       ), tblName("agentProfiles" )) &
    dropForeignKey(fkName("subSstmnt2stmntObj"), tblName("subStatements"    )) &
    dropForeignKey(fkName("activity2stmntObj" ), tblName("activities"       )) &
    dropForeignKey(fkName("stmntRef2stmntObj" ), tblName("stmntRefs"        )) &
    dropForeignKey(fkName("cntxtActvt2actvt"  ), tblName("contextActivities")) &
    dropForeignKey(fkName("cntxtActvt2cntxt"  ), tblName("contextActivities")) &
    dropForeignKey(fkName("stmnt2stmntObj"    ), tblName("statements"    )) &
    dropForeignKey(fkName("cntxt2stmntRef"    ), tblName("contexts"      )) &
    dropForeignKey(fkName("actor2stmntObj"    ), tblName("actors"        )) &
    dropForeignKey(fkName("subStmnt2actor"    ), tblName("subStatements" )) &
    dropForeignKey(fkName("atchmnt2stmnt"     ), tblName("attachments"   )) &
    dropForeignKey(fkName("actor2account"     ), tblName("actors"        )) &
    dropForeignKey(fkName("result2score"      ), tblName("results"       )) &
    dropForeignKey(fkName("stmnt2result"      ), tblName("statements"    )) &
    dropForeignKey(fkName("stmnt2actor"       ), tblName("statements"    )) &
    dropForeignKey(fkName("stmnt2cntxt"       ), tblName("statements"    )) &
    dropForeignKey(fkName("actor2group"       ), tblName("actors"        )) &
    dropIndex(idxName("stmntRef"       ), tblName("stmntRefs"       )) &
    dropIndex(idxName("activities"     ), tblName("activities"      )) &
    dropIndex(idxName("agentProfile"   ), tblName("agentProfiles"   )) &
    dropIndex(idxName("stateProfile"   ), tblName("stateProfiles"   )) &
    dropIndex(idxName("activityProfile"), tblName("activityProfiles")) &
    dropTable(tblName("contextActivities")) &
    dropTable(tblName("statementObjects" )) &
    dropTable(tblName("activityProfiles" )) &
    dropTable(tblName("stateProfiles"    )) &
    dropTable(tblName("agentProfiles"    )) &
    dropTable(tblName("subStatements"    )) &
    dropTable(tblName("attachments"      )) &
    dropTable(tblName("statements"       )) &
    dropTable(tblName("activities"       )) &
    dropTable(tblName("documents"        )) &
    dropTable(tblName("stmntRefs"        )) &
    dropTable(tblName("contexts"         )) &
    dropTable(tblName("accounts"         )) &
    dropTable(tblName("results"          )) &
    dropTable(tblName("scores"           )) &
    dropTable(tblName("actors"           ))

  def upgradeMigrations =
    actors             .create.addColumns &
    scores             .create.addColumns &
    results            .create.addColumns &
    contexts           .create.addColumns &
    accounts           .create.addColumns &
    documents          .create.addColumns &
    activities         .create.addColumns &
    statements         .create.addColumns &
    attachments        .create.addColumns &
    subStatements      .create.addColumns &
    stateProfiles      .create.addColumns &
    agentProfiles      .create.addColumns &
    statementObjects   .create.addColumns &
    activityProfiles   .create.addColumns &
    contextActivities  .create.addColumns &
    statementReferences.create.addColumns &
    statementReferences.addPrimaryKeys &
    contextActivities  .addPrimaryKeys &
    activityProfiles   .addPrimaryKeys &
    statementObjects   .addPrimaryKeys &
    agentProfiles      .addPrimaryKeys &
    stateProfiles      .addPrimaryKeys &
    subStatements      .addPrimaryKeys &
    attachments        .addPrimaryKeys &
    activities         .addPrimaryKeys &
    statements         .addPrimaryKeys &
    documents          .addPrimaryKeys &
    accounts           .addPrimaryKeys &
    contexts           .addPrimaryKeys &
    results            .addPrimaryKeys &
    scores             .addPrimaryKeys &
    actors             .addPrimaryKeys &
    scores             .addForeignKeys &
    actors             .addForeignKeys &
    results            .addForeignKeys &
    accounts           .addForeignKeys &
    contexts           .addForeignKeys &
    documents          .addForeignKeys &
    activities         .addForeignKeys &
    statements         .addForeignKeys &
    attachments        .addForeignKeys &
    agentProfiles      .addForeignKeys &
    stateProfiles      .addForeignKeys &
    subStatements      .addForeignKeys &
    activityProfiles   .addForeignKeys &
    statementObjects   .addForeignKeys &
    contextActivities  .addForeignKeys &
    statementReferences.addForeignKeys &
    statementReferences.addIndexes &
    contextActivities  .addIndexes &
    statementObjects   .addIndexes &
    activityProfiles   .addIndexes &
    agentProfiles      .addIndexes &
    stateProfiles      .addIndexes &
    subStatements      .addIndexes &
    attachments        .addIndexes &
    activities         .addIndexes &
    statements         .addIndexes &
    documents          .addIndexes &
    accounts           .addIndexes &
    contexts           .addIndexes &
    results            .addIndexes &
    actors             .addIndexes &
    scores             .addIndexes

  def up(lrs: Lrs) {
    logger.info("Upgrading to 2.3")

    if(logger.isDebugEnabled)
      logger.debug(upgradeMigrations.migrations.mkString(";\n"))

    if(hasNotTables) {
      logger.info("Applying database schema changes")
      upgrade
    }
    else logger.info("Tables for version 2.3 exists already")

    val dataMigration = new DataUpgrade(lrs.asInstanceOf[JdbcLrs])
    logger.info("Applying data changes")
    dataMigration.upgrade
  }

  def down(lrs: Lrs): Unit = ()
}

