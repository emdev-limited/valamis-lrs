package com.arcusys.valamis.lrs.liferay.history.ver240

import javax.inject.Inject

import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.auth.datasource.{ DataContext => AuthDataContext }
import com.arcusys.valamis.lrs.datasource.     { DataContext => LrsDataContext  }
import com.arcusys.valamis.lrs.liferay.history.BaseDbUpgrade

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade @Inject() (val driver: JdbcDriver,
                                 val db: JdbcBackend#Database) extends BaseDbUpgrade {

  val authDataContext  = new AuthDataContext(driver, db)
  val lrsDataContext   = new LrsDataContext (driver, db)
  val applications = authDataContext.applications.baseTableRow
  val tokens       = authDataContext.tokens      .baseTableRow
  val userApps     = authDataContext.userApps    .baseTableRow

  val agentProfiles = lrsDataContext.agentProfiles.baseTableRow

  val tablesInMigration = Seq(
    applications.tableName,
    tokens      .tableName,
    userApps    .tableName
  )

  def hasNotTables = tables
    .map { t => t.name.name }
    .intersect { tablesInMigration }
    .isEmpty

  def upgradeMigrations =
    applications.create.addColumns &
    tokens      .create.addColumns &
    userApps    .create.addColumns &
    applications.addForeignKeys    &
    tokens      .addForeignKeys    &
    userApps    .addForeignKeys    &
    applications.addIndexes        &
    tokens      .addIndexes        &
    userApps    .addIndexes        &
    agentProfiles.dropForeignKeys  &
    agentProfiles.addForeignKeys
    

  def downgradeMigrations =
    dropForeignKey("fk_token2application", "lrs_tokens"   ) &
    dropIndex("idx_user_app_access", "lrs_user_app_access") &
    dropIndex("idx_app_name"       , "lrs_applications"   ) &
    dropIndex("idx_token"          , "lrs_tokens"         ) &
    dropTable("lrs_user_app_access"                       ) &
    dropTable("lrs_applications"                          ) &
    dropTable("lrs_tokens"                                )
}