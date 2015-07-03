package com.arcusys.valamis.lrs.services

import com.arcusys.valamis.lrs.datasource.DataContext
import com.google.inject.Inject
import org.slf4j.LoggerFactory

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 28/01/15.
 */
private[lrs] class LRS @Inject() (driver: JdbcDriver,
                                  db: JdbcBackend#Database)
  extends DataContext(driver, db)
  with StatementSaverComponent
  with StatementFinderComponent
  with AgentComponent
  with ActivityProfileComponent
  with StateProfileComponent
  with VerbComponent {

  protected lazy val logger  = LoggerFactory.getLogger(getClass)
}