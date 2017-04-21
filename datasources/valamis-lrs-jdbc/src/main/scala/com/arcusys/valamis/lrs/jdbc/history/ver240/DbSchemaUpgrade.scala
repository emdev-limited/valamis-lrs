package com.arcusys.valamis.lrs.jdbc.history.ver240

import javax.inject.Inject

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import com.arcusys.valamis.lrs.jdbc._

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade @Inject() (val jdbcDriver: JdbcDriver,
                                 val database: JdbcBackend#Database,
                                 val lrs: Lrs) extends BaseDbUpgrade {

  val lrsDataContext = lrs.asInstanceOf[JdbcLrs]
  val authDataContext  = new JdbcSecurityManager(jdbcDriver, database)
  val applications = authDataContext.applications.baseTableRow
  val tokens       = authDataContext.tokens      .baseTableRow

  val agentProfiles = lrsDataContext.agentProfiles.baseTableRow

  val tablesInMigration = Seq(
    applications.tableName,
    tokens      .tableName
  ).map { name =>
    jdbcDriver match {
      case OracleDriver => name.toUpperCase()
      case _ => name
    }
  }

  def hasNotTables = tables
    .map { t => t.name.name }
    .intersect { tablesInMigration }
    .isEmpty

  def upgradeMigrations =
    applications.create.addColumns &
      tokens      .create.addColumns &
      applications.addForeignKeys    &
      tokens      .addForeignKeys    &
      applications.addIndexes        &
      agentProfiles.dropForeignKeys  &
      agentProfiles.addForeignKeys &
      optionalUpgradeMigrations

  def optionalUpgradeMigrations = jdbcDriver match {
    case OracleDriver => MigrationSeq()
    case _ => tokens.addIndexes//this index is too big for Oracle
    //trying to create it lead to this error: ORA-01450: maximum key length (6398) exceeded
  }


  def downgradeMigrations =
    optionalDowngradeMigrations &
      dropForeignKey("fk_token2application", "lrs_tokens"   ) &
      dropIndex("idx_app_name"       , "lrs_applications"   ) &
      dropTable("lrs_applications"                          ) &
      dropTable("lrs_tokens"                                )

  def optionalDowngradeMigrations = jdbcDriver match {
    case OracleDriver => MigrationSeq()
    case _ => dropIndex("idx_token", "lrs_tokens")
  }

  override def up(lrs: Lrs): Unit = {
    logger.info("Upgrading to 2.4")

    if(logger.isDebugEnabled)
      logger.debug(upgradeMigrations.migrations.mkString(";\n"))

    if(hasNotTables) {
      logger.info("Applying database schema changes")
      upgrade
    } else {
      logger.info("Tables for version 2.4 exists already")

      val dataMigration = new DataUpgrade(lrs.asInstanceOf[JdbcLrs])
      dataMigration.upgrade
    }
  }

  override def down(lrs: Lrs): Unit = ()
}