package com.arcusys.valamis.lrs.jdbc.database.row

/**
 * Created by Iliya Tryapitsin on 02/01/15.
 */
case class ScoreRow(key: ScoreRow#KeyType = None,
                    scaled: Option[Float] = None,
                    raw: Option[Float] = None,
                    min: Option[Float] = None,
                    max: Option[Float] = None) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}
