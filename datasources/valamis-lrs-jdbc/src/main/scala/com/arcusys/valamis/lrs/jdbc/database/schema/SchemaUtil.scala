package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.BaseDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._

import scala.slick.lifted.TableQuery

/**
 * Created by Iliya Tryapitsin on 22.07.15.
 */
trait SchemaUtil {
  this: BaseDataContext =>

  val TQ = TableQuery
  type ?[A] = Option[A]

  import executionContext.driver.simple._

  abstract class LongKeyTable[E](tag: Tag, name: String, useAutoInc: Boolean = true) extends Table[E](tag, name) {
    def key = if (useAutoInc)
      column[Long]("key", O.PrimaryKey, O.AutoInc)
    else
      column[Long]("key", O.PrimaryKey)
  }

  abstract class UUIDKeyTable[E](tag: Tag, name: String) extends Table[E](tag, name) {

    def key = column[String]("key", O.PrimaryKey, O.DBType(uuidKeyLength))
  }

  abstract class StringKeyTable[E](tag: Tag, name: String) extends Table[E](tag, name) {
    def key = column[String]("key", O.PrimaryKey, O.DBType(varCharPk))
  }
}
