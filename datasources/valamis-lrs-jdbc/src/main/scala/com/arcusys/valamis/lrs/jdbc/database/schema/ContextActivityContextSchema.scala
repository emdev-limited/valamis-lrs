package com.arcusys.valamis.lrs.jdbc.database.schema

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.row._
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils
import com.arcusys.valamis.lrs.jdbc.database.utils.DbNameUtils._

trait ContextActivityContextSchema extends SchemaUtil {
  this: LrsDataContext =>

  import driver.simple._

  class ContextActivitiesContextTable(tag: Tag) extends LongKeyTable[ContextActivityContextRow](tag: Tag, tblName("contextActivitiesContext")) {
    override def * = (key.?, contextKey, activityKey) <>(ContextActivityContextRow.tupled, ContextActivityContextRow.unapply)

    def contextKey = column[ContextRow#Type]("contextKey", O.NotNull,O.DBType(uuidKeyLength))

    def activityKey = column[ContextActivityActivityRow#Type]("activityKey", O.NotNull)

    def context = foreignKey(fkName("cntxtActvt2cntxtCntxt"), contextKey, TableQuery[ContextsTable])(x => x.key)

    def activity = foreignKey(fkName("cntxtActvt2actvtCntxt"), activityKey, TableQuery[ContextActivitiesActivityTable])(x => x.key)


    def conextActivityKeyIndx = index("idx_context_activity_key", activityKey)

    def contextContextKeyIndx = index("idx_context_context_key", contextKey)
  }

  lazy val contextActivitiesContext = TableQuery[ContextActivitiesContextTable]

}
