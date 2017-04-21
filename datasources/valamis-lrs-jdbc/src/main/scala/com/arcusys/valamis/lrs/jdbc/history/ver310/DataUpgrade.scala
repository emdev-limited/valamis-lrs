package com.arcusys.valamis.lrs.jdbc.history.ver310

import java.util.UUID

import com.arcusys.valamis.lrs.jdbc.JdbcLrs
import com.arcusys.valamis.lrs.jdbc.history.BaseUpgrade

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend


class DataUpgrade(val lrs: JdbcLrs,
                  val jdbcDriver: JdbcDriver,
                  val database: JdbcBackend#Database) extends BaseUpgrade {

  import jdbcDriver.simple._

  def upgrade = {
    fixActivities
  }


  private def fixActivities = {
    logger.info("Fix activities")

    val oldActivityTable = new ActivitySchema(jdbcDriver, database)

    database.withTransaction { implicit session =>
      val emptyRows = oldActivityTable.activities
        .filter { x => x.id.isEmpty || x.id === "" }
        .map { a => a.key }
        .list

        emptyRows.foreach { key =>
          oldActivityTable.activities
            .filter(_.key === key)
            .map(_.id)
            .update(Some(s"http://${UUID.randomUUID}"))
        }

    }

  }
}
