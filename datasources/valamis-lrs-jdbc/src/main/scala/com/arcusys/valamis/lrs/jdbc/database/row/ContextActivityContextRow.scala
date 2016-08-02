package com.arcusys.valamis.lrs.jdbc.database.row

case class ContextActivityContextRow(key: ContextActivityContextRow#KeyType = None,
                                     contextKey: ContextRow#Type,
                                     activityKey: ContextActivityActivityRow#Type) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}