package com.arcusys.valamis.lrs.jdbc.history.init

import javax.inject.Inject

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.slick.migration.dialect.Dialect
import com.arcusys.slick.migration.table.TableMigration
import com.arcusys.valamis.lrs.DbInit
import com.arcusys.valamis.lrs.jdbc.Loggable
import com.arcusys.valamis.lrs.jdbc.database.{LrsDataContext, SecurityDataContext}

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.meta.MTable

class DbSchemaInit @Inject()(val driver: JdbcDriver,
                             val db: JdbcBackend#Database) extends DbInit
  with LrsDataContext with SecurityDataContext with Loggable {

  implicit lazy val dialect = Dialect(driver)
    .getOrElse(throw new Exception(s"There is no dialect for driver ${driver}"))

  val schemaTables = Seq(
    actors,
    scores,
    results,
    accounts,
    contexts,
    documents,
    activities,
    statements,
    attachments,
    subStatements,
    agentProfiles,
    stateProfiles,
    activityProfiles,
    statementObjects,
    contextActivitiesContext,
    contextActivitiesActivity,
    statementReferences,
    applications,
    tokens
  )
  val tablesInMigration = schemaTables.map(_.baseTableRow.tableName)
    .map { name =>
      driver match {
        case OracleDriver => name.toUpperCase()
        case _ => name
      }
    }

  def hasTables: Boolean = {
    lazy val currentTables = tables
    tablesInMigration.count(tm => currentTables.exists(t => t.name.name == tm)) == tablesInMigration.size
  }

  private def tables: Seq[MTable] = db.withSession { implicit session =>
    driver.defaultTables
  }
}

