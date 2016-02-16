package com.arcusys.valamis.lrs.jdbc.database

import com.arcusys.valamis.lrs.jdbc.ExecutionContext
import com.arcusys.valamis.lrs.jdbc.database.schema.SchemaUtil
import com.arcusys.valamis.lrs.jdbc.database.typemap._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.JodaSupport
import com.arcusys.valamis.lrs.jdbc.database.utils.CustomOperator

import scala.slick.jdbc.JdbcBackend

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
abstract class BaseDataContext
  extends TypeMapper
  with SchemaUtil
  with CustomOperator
  with CustomTypeExtension {
    val db:               JdbcBackend#Database
    val jodaSupport:      JodaSupport
    val executionContext: ExecutionContext
}