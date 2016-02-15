package com.arcusys.valamis.lrs.jdbc.database

import com.arcusys.valamis.lrs.jdbc.database.schema.{TokenSchema, ApplicationSchema}

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait SecurityDataContext
  extends BaseDataContext
  with ApplicationSchema
  with TokenSchema