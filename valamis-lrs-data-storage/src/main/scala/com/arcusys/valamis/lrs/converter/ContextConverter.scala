package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{ContextRow, StatementReferenceRow, GroupRow, StatementObjectRow}
import com.arcusys.valamis.lrs.tincan.Context

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object ContextConverter {
  implicit def asRow(value: Context,
                     instructor: Option[StatementObjectRow#Type] = None,
                     team: Option[GroupRow#Type] = None,
                     statement: Option[StatementReferenceRow#Type] = None): ContextRow = ContextRow(
    instructor = instructor,
    team = team,
    revision = value.revision,
    platform = value.platform,
    language = value.language,
    statement = statement,
    extensions = value.extensions)
}
