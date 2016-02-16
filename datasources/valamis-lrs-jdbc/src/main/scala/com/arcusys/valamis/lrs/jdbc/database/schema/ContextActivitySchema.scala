package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import DbNameUtils._
import com.arcusys.valamis.lrs.jdbc.database.row._

/**
 * Created by Iliya Tryapitsin on 23.07.15.
 */
trait ContextActivitySchema extends SchemaUtil {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  class ContextActivitiesTable(tag: Tag) extends Table[ContextActivityRow](tag: Tag, tblName("contextActivities")) {
    override def * = (contextKey, activityKey, contextActivityType) <>(ContextActivityRow.tupled, ContextActivityRow.unapply)

    def contextKey = column[ContextRow#Type]("contextKey", O.NotNull,O.DBType(uuidKeyLength))
    def activityKey = column[ActivityRow#Type]("activityKey", O.NotNull)
    def contextActivityType = column[ContextActivityType.Type]("type", O.NotNull, O.DBType(varCharPk))

    def pk = primaryKey(pkName("contextActivities"), (contextKey, activityKey, contextActivityType))

    def context = foreignKey(fkName("cntxtActvt2cntxt"), contextKey, TableQuery[ContextsTable])(x => x.key)
    def activity = foreignKey(fkName("cntxtActvt2actvt"), activityKey, TableQuery[ActivitiesTable])(x => x.key)
  }

  lazy val contextActivities = TableQuery[ContextActivitiesTable]

}
