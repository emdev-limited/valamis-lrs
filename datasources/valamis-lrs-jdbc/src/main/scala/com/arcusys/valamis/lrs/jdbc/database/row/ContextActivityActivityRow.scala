package com.arcusys.valamis.lrs.jdbc.database.row

case class ContextActivityActivityRow(key: ContextActivityActivityRow#KeyType = None,
                                      activityKey: ActivityRow#Type,
                                      tpe: ContextActivityType.Type) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}