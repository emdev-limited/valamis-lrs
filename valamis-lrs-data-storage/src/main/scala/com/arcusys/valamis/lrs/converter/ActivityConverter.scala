package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{ActivityRow, StatementObjectRow}
import com.arcusys.valamis.lrs.tincan.Activity

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object ActivityConverter {
  implicit def asRow(activity: Activity, statementObjectRowKey: StatementObjectRow#Type): ActivityRow = ActivityRow(
    statementObjectRowKey, 
    activity.id,
    activity.name, 
    activity.description, 
    activity.theType,
    activity.moreInfo,
    activity.interactionType,
    activity.correctResponsesPattern,
    activity.choices,
    activity.scale,
    activity.source,
    activity.target,
    activity.steps, 
    activity.extensions)

  implicit def asModel(row: ActivityRow): Activity = Activity(
    row.id,
    row.name,
    row.description,
    row.theType,
    row.moreInfo,
    row.interactionType,
    row.correctResponsesPattern,
    row.choices,
    row.scale,
    row.source,
    row.target,
    row.steps,
    row.extensions)
}
