package com.arcusys.valamis.lrs.converter

import com.arcusys.valamis.lrs.datasource.row.ScoreRow
import com.arcusys.valamis.lrs.tincan.Score

/**
 * Created by Iliya Tryapitsin on 25/02/15.
 */
object ScoreConverter {
  def asRow(value: Score): ScoreRow = ScoreRow(
    scaled = value.scaled,
    raw = value.raw, 
    min = value.min, 
    max = value.max)

  def asModel(value: ScoreRow): Score = Score(
    value.scaled, 
    value.raw, 
    value.min, 
    value.max)

}
