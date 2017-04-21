package com.arcusys.valamis.lrs.jdbc

import javax.inject.Inject

import com.arcusys.valamis.lrs.{LrsImpl}

import scala.concurrent.ExecutionContextExecutor
import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

class JdbcLrs @Inject() (val driver: JdbcDriver,
                         val db: JdbcBackend#Database,
                         val executor: ExecutionContextExecutor)
  extends LrsImpl
  with JdbcLrsComponent