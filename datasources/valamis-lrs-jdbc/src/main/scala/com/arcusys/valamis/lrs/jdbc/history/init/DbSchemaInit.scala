package com.arcusys.valamis.lrs.jdbc.history.init

import java.sql.SQLException
import javax.inject.Inject

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.slick.migration.dialect.Dialect
import com.arcusys.valamis.lrs.DbInit
import com.arcusys.valamis.lrs.jdbc.database.{LrsDataContext, SecurityDataContext}
import org.apache.commons.logging.Log

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.meta.MTable
import scala.slick.jdbc.{JdbcBackend, StaticQuery}

class DbSchemaInit @Inject()(val driver: JdbcDriver,
                             val db: JdbcBackend#Database,
                             val logger: Log) extends DbInit
  with LrsDataContext with SecurityDataContext {

  private implicit lazy val dialect = Dialect(driver)
    .getOrElse(throw new Exception(s"There is no dialect for driver $driver"))

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
  private val tablesInMigration = schemaTables.map(_.baseTableRow.tableName)
    .map { name =>
      driver match {
        case OracleDriver => name.toUpperCase()
        case _ => name
      }
    }

  def hasTables: Boolean = {
    driver match {
      case OracleDriver => tablesInMigration forall hasTableInOracle
      case _ =>
        val currentTables = tables
        tablesInMigration.count(tm => currentTables.exists(t => t.name.name == tm)) == tablesInMigration.size
    }

  }

  private def hasTableInOracle(tableName: String): Boolean = {
    db.withSession { implicit s =>
      try {
        StaticQuery.queryNA[String](s"SELECT * FROM $tableName WHERE 1 = 0").list
        true
      } catch {
        case _: SQLException => false
      }
    }
  }

  private def tables: Seq[MTable] = db.withSession { implicit session =>
    driver.defaultTables
  }
}

