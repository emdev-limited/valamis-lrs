package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.{ResultRow, ScoreRow}
import com.arcusys.valamis.lrs.tincan.Result

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object ResultConverter {
  implicit def asRow(value: Result,
                     scoreId: ScoreRow#KeyType = None): ResultRow = ResultRow(
    scoreId = scoreId,
    success = value.success,
    completion = value.completion,
    response = value.response,
    duration = value.duration,
    extensions = value.extensions)
}
