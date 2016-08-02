package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils._

trait ContextActivityActivitySchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class ContextActivitiesActivityTable(tag: Tag) extends LongKeyTable[ContextActivityActivityRow](tag: Tag, tblName("contextActivitiesActivity")) {
    override def * = (key.?, activityKey, contextActivityType) <>(ContextActivityActivityRow.tupled, ContextActivityActivityRow.unapply)

    def activityKey = column[ActivityRow#Type]("activityKey", O.NotNull)

    def contextActivityType = column[ContextActivityType.Type]("type", O.NotNull, O.DBType(varCharPk))

    def activity = foreignKey(fkName("cntxtActvt2actvtActtvt"), activityKey, TableQuery[ActivitiesTable])(x => x.key)

    def activitiesKeyIndx = index("idx_activities_activity_key", activityKey)

  }

  lazy val contextActivitiesActivity = TableQuery[ContextActivitiesActivityTable]

}
