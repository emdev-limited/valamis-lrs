package com.arcusys.valamis.lrs.jdbc

import javax.inject.{Named, Inject}
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.JodaSupport

import scala.concurrent.ExecutionContextExecutor
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 04.08.15.
 */
class SimpleLrs @Inject() (val db: JdbcBackend#Database,
                           @Named("Simple") val executionContext: ExecutionContext,
                           @Named("Simple") val jodaSupport: JodaSupport,
                           @Named("Simple") val executor: ExecutionContextExecutor)
  extends JdbcLrs