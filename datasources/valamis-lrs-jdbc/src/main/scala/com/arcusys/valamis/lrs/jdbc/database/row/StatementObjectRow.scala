package com.arcusys.valamis.lrs.jdbc.database.row

import com.arcusys.valamis.lrs.tincan.StatementObjectType.Type

/**
 * Created by igorborisov on 15.01.15.
 */

case class StatementObjectRow(key: StatementObjectRow#KeyType = None,
                              objectType: Type) extends WithOptionKey[Long] {
  override def withId[M](e: M) = copy(key = e.asInstanceOf[KeyType])
}
