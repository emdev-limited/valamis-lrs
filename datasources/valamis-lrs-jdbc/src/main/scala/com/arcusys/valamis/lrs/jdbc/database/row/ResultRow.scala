package com.arcusys.valamis.lrs.jdbc.database.row

import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 02/01/15.
 */
case class ResultRow(key: ResultRow#KeyType = None,
                     scoreId: ScoreRow#KeyType,
                     success: Option[Boolean],
                     completion: Option[Boolean],
                     response: Option[String],
                     duration: Option[String],
                     extensions: Option[ExtensionMap]) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}