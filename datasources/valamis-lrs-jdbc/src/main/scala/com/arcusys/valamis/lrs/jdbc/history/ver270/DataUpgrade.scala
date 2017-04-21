package com.arcusys.valamis.lrs.jdbc.history.ver270

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.valamis.lrs.jdbc.history.BaseUpgrade
import com.arcusys.valamis.lrs.jdbc.JdbcLrs

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.{JdbcBackend, StaticQuery}


class DataUpgrade(val lrs: JdbcLrs,
                  val jdbcDriver: JdbcDriver,
                  val database: JdbcBackend#Database) extends BaseUpgrade {

  import jdbcDriver.simple._

  val sqlQueries: Seq[String] = Seq(
    "UPDATE LRS_STATEPROFILES SET REGISTRATION = ' ' WHERE REGISTRATION IS NULL",
    "UPDATE LRS_TOKENS SET VERIFIER = ' ' WHERE VERIFIER IS NULL",
    "UPDATE LRS_TOKENS SET TOKEN = ' ' WHERE TOKEN IS NULL",
    "UPDATE LRS_TOKENS SET TOKENSECRET = ' ' WHERE TOKENSECRET IS NULL",
    "UPDATE LRS_ACTIVITIES SET TYPE= ' ' WHERE TYPE IS NULL",
    "UPDATE LRS_ACTIVITIES SET MOREINFO= ' ' WHERE MOREINFO IS NULL"
  )

  def upgrade = lrs.db.withSession { implicit session =>
    tryAction {
      updateAccounts
      replaceNullValues
    }

  }

  def tables = database.withSession { implicit session =>
    jdbcDriver.defaultTables
  }

  private def replaceNullValues(implicit session: JdbcBackend#Session) = {
    //replace all NULL values in Option[String] columns by ' '
    //accordingly with OracleDriver behavior (it never saves null or empty string as NULL, but as ' ' instead)
    if (jdbcDriver.isInstanceOf[OracleDriver]) {
      logger.info("Replace null values for Oracle")
      sqlQueries.foreach(StaticQuery.updateNA(_).execute)
    }
  }

  private def updateAccounts(implicit session: JdbcBackend#Session) = {
    logger.info("Update accounts")

    lrs.accounts.filter(_.name === "").map(_.name).update("no name")
  }
}


