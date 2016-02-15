package com.arcusys.valamis.lrs.liferay.history.ver250

import javax.inject.Inject

import com.arcusys.slick.migration._
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.SimpleJodaSupport
import com.arcusys.valamis.lrs.liferay.history.BaseDbUpgrade

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade @Inject() (val jdbcDriver: JdbcDriver,
                                 val database: JdbcBackend#Database) extends BaseDbUpgrade {
  val jodaSupport      = new SimpleJodaSupport(jdbcDriver)
  val executionContext = new SimpleExecutionContext(jdbcDriver, database)
  val authDataContext  = new SimpleSecurityManager(database, executionContext, jodaSupport)

  val tokens = authDataContext.tokens.baseTableRow

  val tablesInMigration = Seq(
    tokens.tableName
  )

  def upgradeMigrations = MigrationSeq(
    tokens.alterColumnType(_.callback)
  )


  def downgradeMigrations = throw new Exception("Can't apply migration 'cause it contains irreversible steps")

}