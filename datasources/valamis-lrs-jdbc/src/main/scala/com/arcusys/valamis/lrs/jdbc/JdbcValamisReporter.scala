package com.arcusys.valamis.lrs.jdbc

import javax.inject.Inject

import com.arcusys.valamis.lrs.ValamisReporterImpl

import scala.concurrent.ExecutionContextExecutor
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 04.08.15.
 */
class JdbcValamisReporter @Inject() (val db: JdbcBackend#Database,
                                       val driver: JdbcDriver,
                                       val executor: ExecutionContextExecutor)
  extends ValamisReporterImpl with JdbcValamisReporterComponent